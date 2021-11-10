package net.ausiasmarch.blogBusterSB;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostRepository oPostRepository;

    @GetMapping("/{id}")
    public ResponseEntity<PostEntity> get(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<PostEntity>(oPostRepository.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> index() {
        List<PostEntity> listAll = null;
        Map<String, Object> response = new HashMap<>();
        listAll = oPostRepository.findAll();
        response.put("size", listAll.size());
        response.put("rows", listAll);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> indexOrderBy(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer rpp,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "asc")String dir,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String cuerpo,
            @RequestParam(required = false) LocalDateTime fecha,
            @RequestParam(required = false) String etiquetas) {

        List<PostEntity> listAll = null;
        Pageable paging;
        if(dir.equalsIgnoreCase("asc")){
        paging = PageRequest.of(page, rpp, Sort.by(order));}
        else{
        paging = PageRequest.of(page, rpp, Sort.by(order).descending());}
        Map<String, Object> response = new HashMap<>();
        Page<PostEntity> pagePost;
        if (id == null && titulo == null && cuerpo == null && fecha == null && etiquetas == null) {
            pagePost = oPostRepository.findAll(paging);
        } else if (id != null) {
            pagePost = oPostRepository.findById(id, paging);
        } else if (titulo != null) {
            pagePost = oPostRepository.findByTitulo(titulo, paging);
        } else if (cuerpo != null) {
            pagePost = oPostRepository.findByCuerpo(cuerpo, paging);
        } else if (fecha != null) {
            pagePost = oPostRepository.findByFecha(fecha, paging);
        } else if (etiquetas != null) {
            pagePost = oPostRepository.findByEtiquetas(etiquetas, paging);
        } else {
            pagePost = oPostRepository.findAll(paging);
        }

        listAll = pagePost.getContent();
        response.put("size", listAll.size());
        response.put("rows", listAll);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        oPostRepository.deleteById(id);
    }

    @PostMapping("/create")
    public PostEntity create(@RequestBody PostEntity newPostEntity) {
        return oPostRepository.save(newPostEntity);
    }

    @PutMapping("/update")
    public PostEntity update(@RequestBody PostEntity newPostEntity) {
        if (oPostRepository.findById(newPostEntity.getId()).isPresent()) {
            PostEntity updtPostEntity = oPostRepository.findById(newPostEntity.getId()).get();
            updtPostEntity.setTitulo(newPostEntity.getTitulo());
            updtPostEntity.setCuerpo(newPostEntity.getCuerpo());
            updtPostEntity.setFecha(newPostEntity.getFecha());
            updtPostEntity.setEtiquetas(newPostEntity.getEtiquetas());
            updtPostEntity.setVisible(newPostEntity.getVisible());
            oPostRepository.save(updtPostEntity);
            return updtPostEntity;
        } else {
            return null;
        }

    }

}
