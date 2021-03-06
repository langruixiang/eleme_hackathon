##简介
本项目是参加eleme全国大学生黑客马拉松大赛做的项目，项目总体目标是实现一个定时抢购系统，在通过功能测试的基础上比系统性能，具体规则请参看RULE.md，本项目采用Java实现。elemeServer文件夹是项目的源代码。compile.sh以命令行的方式编译源代码，run.sh以命令行的方式运行服务器。其他文件参看RULE.md即可知道其用途。比赛成绩一般，没有进入第二轮，服务器端采用Netty处理http请求，后台采用mysql数据库，和redis内存数据库。
##收获
虽然没有进入第二轮，但是还是有收获的。最大的收获是看到公司做一个项目的方式，工欲善其事，必先利其器，不同阶段采用合适的工具确实会事半功倍。</br>

* **开发环境的搭建**: vagrant + virtualbox</br>
vagrant和virtualbox真是搭建开发环境的利器。以前自己写的项目，要在别人电脑运行起来，想想都觉得是噩梦，既要装这个，又要装那个，搞来搞去还容易出错。用vagrant + virtualbox可以快速把自己的开发环境让别人用上，再配置端口映射，共享目录，让对方还能在宿主机上直接做开发，真的是很方便。
* **Git**：
早就知道git和方便，但是一直没有使用，读了本科读硕士，一直在学校的大环境下做开发，项目人数一般都不是很多，一般也就用QQ，邮件，u盘传来传去，觉得也挺方便。真正用了git之后才发现，以前的效率是多么低下。最关键的还是安全，直接托管在远程仓库，总不会因为自己的电脑出现问题的从0开始。现在写代码，即便是一个人的做的小项目，也强迫症般的必须托管在远程仓库，不然没有安全感。
* **GitLab**：</br>
说道git远程仓库，自然而让会想到github，但是github的缺点就是默认开源，私有仓库的价钱还是很昂贵的。这次比赛eleme公司使用的是gitlab，免费私有仓库，用起来体验还是不错的。
* **pytest**：</br>
虽然没接触过软件测试相关的内容，但是这次还是感受到了pytest或者说自动化测试的强大，写完代码，一个命令立马可以看到测试数据的执行情况，还是很方便的。作为一个主要使用Java的程序猿，自然知道了Junit还是很好用的。
* **CI（Continuous Integration）**：</br>
比赛的时候，每次代码push到gitlab之后，自动的就看到代码的构建结果和测试结果，才知道有CI这个强大的东西，网上查了查github也支持接入CI。接入CI的一个好处就是，每次push上去之后，自动会构建项目，并执行自动化测试。 这样很多人合作做一个项目的时候，提前吧测试用例写好，每个人push的时候就知道自己的版本有没有引入bug。当然了，CI的功能不仅仅是这个，还有很多强大的功能。
* **Netty**:</br>
比赛的时候，觉得自己写服务器一来效率可能不高，二来花费时间可能比较长。Netty作为高性能的异步网络开发框架，自然就想到了使用Netty。使用Netty构建一个http服务器还是很方便的，相比于Tomcat之类的容器，还是轻便了许多。性能方便，Netty作为阿里RocketMQ底层通讯框架，其性能应该是可圈可点的。但是作为Netty菜鸟，自己有没有把Netty的性能发挥出来就不知道了。不管怎样，也跳了一些Netty的坑，下次使用Netty不会像这次那么茫然了，也许用得多了也就知道了怎样更好地使用Netty.
* **优化系统/优化算法**:</br>
这次比赛，做出来第一个通过测试的版本后，性能只有一点点提升。觉得我们优化的方向是错的。优化系统和优化算法还是有很大区别的，优化算法总想着把算法复杂度降低一个量级，比如从O(n<sup>2</sup>)降低到O(nlogn)了。但是优化系统就不一样了，就算算法复杂度降了一个量级，系统性能可能根本没有变化，因为，系统的瓶颈压根没在cpu。优化系统最重要的还是找系统的瓶颈，而不是盲目的优化系统用到的算法。现在本人觉得本次系统的瓶颈应该在IO。我们想过Redis服务器和应用服务器存在通讯量过大的问题，但是我们觉得局域网内网速是很快的，就放弃优化这个内容。其实，这里犯了一个逻辑错误，即便网速再快，就算完全忽略了网络传输的时间，那么网络操作也是IO操作。也就是说完全忽略网速的情况下系统使用网络数据应该是和使用硬盘数据在差不多的量级上.我们自然知道要尽量少读硬盘数据，少使用数据库，却觉得网络通讯的数据量大点对系统影响不大，确实是被网速这个东西给绕进去了。
* **工欲善其事，必先利其器**：</br>
最后还是回到了这个问题，在讨论系统瓶颈的时候，其实我们也是有分歧的，队友一直在优化Redis，但是没有效果，自然是怀疑Netty处理请求是瓶颈。我也试图优化过Netty，但是觉得也没什么效果，可毕竟是第一次使用Netty，所以心里对Netty这块也没底。也不敢肯定瓶颈就一定不在Netty。这个时候我们最应该做的其实是给各自的说法找依据。怎么找，最好的方法是模仿eleme赛题描述的环境，实际去测系统的瓶颈究竟在哪。我们当时也试图用笔记本搭建一个集群去做模拟，后来，搭建的过程遇到了一些波折，或是时间太紧，最关键的还是没有意识到这个模拟环境的重要性，就没有继续搭建。现在想想，既然是做系统优化，其实应该把搭建这个模拟环境的优先级放在很高，这个模拟环境才是我们工作方向的指示标。没有把模拟环境搭建好，就在盲目优化，就好像拿着一把不够锋利的砍刀在砍柴，使再大的劲效果也微乎其微。

