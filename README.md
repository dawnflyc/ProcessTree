# ProcessTree

## 树形流程框架

### 	1、框架什么时候用？

​	写Minecraft mod的时候，经常创建实例阵列，例如：

```java
		new A方块("a");

		new B方块("b");
	
		new C方块("c");
```

​	这样的实例阵列，非常难看，并且浪费时间和力气。

​	本项目是用注解来实现的树形结构，只要这样：

```java
        @TreeHandle(packageName="方块所在包"，api=方块注册器.block.class)
        public class 方块注册器{
            @TreeNotice
            public void 注册方块(){注册方块代码}
             public interface Block{}
        }
```

​	方块类只需要继承block接口，在构造或者接口方法种写上对应代码，便可以自动注册。

​	如果也需要注册物品的话，便可以写上注册类，物品注册类，方块注册类，如同上文形成连锁。

### 	2、注解具体的用法。

​	TreeHandle：类的注解，表示当前的支，用此类来处理分支。

​	TreeNotice ：方法的注解，通知注解，方法要有参数才会去加载，参数有两种写法：

​		Class<接口> [] ：所有的分支类

​		Class<接口> ：不用遍历，直接可以处理

​	如果没有TreeNotice 注解的话，会在分支种查找。如果有注TreeHandle注解的分支，会继续处理。没有的话，会构造个实例，用于在构造方法中处理
