package com.skyscraper.bot.repository;

import java.util.Optional;

import com.skyscraper.bot.entity.impl.UserEntityImpl;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntityImpl, Long> {

  Optional<UserEntityImpl> findByName(String name);

}
