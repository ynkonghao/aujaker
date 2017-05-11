package org.konghao.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBaseService<T,ID> {
	
	public void add(T t);
	public void update(T t);
	public void delete(ID id);
	public T load(ID id);
	public List<T> list();
	public Page<T> find(Pageable page);
}
