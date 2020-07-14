package dawnfly.example;

/**
 * @program: ProcessTree
 * @description: 实现此接口的类，便是Root的分支，树结构类似
 * @author: xiaofei
 * @create: 2018-12-10 16:53
 **/
public interface RootAPI {
    /**
     * 写个方法可以让父支更好的操纵子支
     */
    void run();

}
