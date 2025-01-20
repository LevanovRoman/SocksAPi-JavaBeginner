package com.myapp.socksapijavabeginner.repository;

import com.myapp.socksapijavabeginner.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SockRepository extends JpaRepository<Sock, Long> {

    Optional<Sock> findByColorAndCottonPercentage(String color, int cottonPercentage);

}
