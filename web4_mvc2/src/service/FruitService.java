package service;

import pojo.Fruit;

import java.util.List;

/**
 * @author Jayce Xu
 * @create 2022504
 */
public interface FruitService {
    List<Fruit> getFruitList(String keyword, Integer pageNo);

    void addFruit(Fruit fruit);

    Fruit getFruitByFid(Integer fid);

    void delFruit(Integer fid);

    Integer getPageCount(String keyword);

    void updateFruit(Fruit fruit);
}
