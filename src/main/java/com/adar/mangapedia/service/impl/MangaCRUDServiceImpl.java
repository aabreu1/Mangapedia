package com.adar.mangapedia.service.impl;

import com.adar.mangapedia.dto.MangaDTO;
import com.adar.mangapedia.mapper.MangaMapper;
import com.adar.mangapedia.model.Manga;
import com.adar.mangapedia.reporsitory.MangaRepository;
import com.adar.mangapedia.service.MangaCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MangaCRUDServiceImpl implements MangaCRUDService {

    private final MangaRepository mangaRepository;

    @Autowired
    public MangaCRUDServiceImpl(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }


    @Override
    public ResponseEntity<String> createManga(MangaDTO manga) {
        try {
            if( mangaRepository.findByName(manga.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manga [" + manga.getName() + "] ya existe");
            }
            mangaRepository.save(MangaMapper.mapTo(manga));
            return ResponseEntity.ok("Manga ["+manga.getName()+"] Registrado");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar ["+manga.getName()+"]");

        }
    }

    @Override
    public ResponseEntity readManga(String mangaName) {
        try {
            Optional<Manga> manga = mangaRepository.findByName(mangaName);
            if (manga.isPresent()) {
                return ResponseEntity.ok(MangaMapper.mapTo(manga.get()));
            } else {
                return ResponseEntity.ok("Manga [" + mangaName + "] no encontrado");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar ["+mangaName+"]");
        }
    }
    @Override
    public ResponseEntity<String>  updateManga(MangaDTO manga) {
        try{
            if(mangaRepository.findById(manga.getId()).isPresent())
            {
                mangaRepository.save(MangaMapper.mapTo(manga));
                return ResponseEntity.ok("Manga ["+manga.getName()+"] Actualizado");

            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manga Id: [" + manga.getId() + "] no existe");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar ["+manga.getId()+"]");
        }
    }


    @Override
    public ResponseEntity<String>  deleteManga(String mangaName) {
        try {
            Optional<Manga> manga = mangaRepository.findByName(mangaName);
            if(manga.isPresent()) {
                mangaRepository.delete(manga.get());
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manga Id: [" + mangaName + "] no existe");
            }
            return ResponseEntity.ok("Manga [" + mangaName + "] Eliminado");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar [" + mangaName + "]");
        }
    }
}
