# ProcessTree

## 树形流程框架

### 一、框架什么时候用？

​	写Minecraft mod的时候，经常创建实例阵列，例如：

```java
		new A方块("a");

		new B方块("b");
	
		new C方块("c");
```

 这样的实例阵列，非常难看，并且浪费时间和力气。

本框架是用注解来标记扫描数据，然后根据扫描范围、优先度、目标等参数来进行扫描，统一处理。

其他参数都有默认值，只有扫描目标需要手动设置，具体用法看下文，这里只需要大概了解一下即可。

```java
        @ScanNode(packageName=auto,target= IBlockRegistered.class) //扫描注解，扫描该类所在包的所有实现IBlockRegistered的类
        public class BlockRegistry implements IScanResultHandler<IBlockRegistered>{ //实现IScanResultHandler接口，便可处理扫描结果
            
         	@Override
    		public void handle(Result<IBlockRegistered> result) {
        		result.build().forEach(iBlockRegistered -> REG_BLOCKS.add(iBlockRegistered.getBlock()));
            	//构建所有类并且放入REG_BLOCKS,这里所有类都有无参构造，注册事件将注册此集合所有对象，注册代码不作展示
   			}
        }
```

方块类只需要继承IBlockRegistered类，便可以自动注册。

### 二、说明

在游戏里，通常需要注册许多预制件，比如物品、方块、实体。

而注册预制件基本上都会在某个方法里堆一堆几乎一样的注册语句，非常地难受。

**而MC预制件也基本上分为两种，简单的和复杂的。**

简单的只需要new预制件，然后修改其构造参数即可实现功能， 比如没有任何功能的方块、物品，木头、木板、小麦、木棍。

复杂的则需要创造类并且实现预制件，从而重写方法来实现稍微复杂点的功能。

**此框架只适用于复杂预制件。**

框架的作用是扫描类，然后构建成对象，用于其他的用途，免去自己写各种阵列，尤其是注册的时候。

由于是扫描一堆类，然后一起构建，所以构造参数也需要一致。

 

**！！！此框架在打包时应打入包内，所以说并不算前置！！！**

### 三、用法

**首先启动服务，它才会开始扫描，因为程序都有个入口。**

```
ProcessTree tree = new ProcessTree(this.getClass().getPackage().getName());
tree.start();
```

参数是扫描的范围，也就是包名，给的是此类所在的包。

注册类就如同上面代码那样写，写上注解、实现接口、重写接口的方法，将会自动调用接口的方法，从而自动处理扫描结果。

但是扫描时，有可能需要扫描的那些类并没有共同的基类，这时可以自定义一个接口。

接口里面可以写上方法，比如获取要注册的方块，可以写一个getBlock()，因为注册方块需要方块对象，这时就可直接调用getBlock()。

### 五、如何在Gradle中导入

打开build.gradle

``

```
compile fileTree(dir:'libs',includes:['*jar'])
```

这段代码放入dependencies中，导入libs中所有库。

```
from {
    files("libs/ProcessTree.jar").collect{ it.isDirectory() ? it : zipTree(it) }
}
```

这段放入jar里，打包时会将框架打入包内。）

### 四、所有用到的类

**1、**    **ScanNode**：扫描节点类，注解，有四个参数：

​                        **packageName**：扫描的范围 ，有两个内部提供的值，auto 类所在的包、all 整个jar，默认 auto

​                        **recursive** ： 是否迭代，也就是包内包，默认：true

​                        **priority** ： 优先级，大的先运行，默认：0

​                        **target** ： 目标，只有这个类或接口的子类才会被扫描，如果是Object则会扫描范围内所有类，并且不会扫描到其自身。

**2、**    **IScanResultHandler**<T>扫描结果处理类，有一个方法

​                    **handle**(Result<T> var1) 实现了接口，框架便会自动调用此方法，用于处理扫描结果。

**3、Result**  扫描结果类，里面有着扫描结果的集合，并且还有一些构建对象方法，先设置构造参数（无参则免），然后再构建，即可获取对象集合。

​						