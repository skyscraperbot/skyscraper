package com.skyscraper.bot.entity;

public interface Entity<T> {

  T getIdentifier();

  void setIdentifier(T identifier);

}
