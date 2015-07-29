BlurStaggered 高斯模糊下拉刷新瀑布流
=====================

My Blog--[MrFu-傅圆的个人博客](http://mrfufufu.github.io/)

Rarely updated blog, but I will post more project to github, I will try to blog much more, although my poor literary talent, but also how good the technology is not necessarily

## 能学到的技能点

* 高斯模糊(Blur)
* 瀑布流(StaggeredGridView) 
* 基于瀑布流的下拉刷新(PullRefreshStaggeredListView)
* 图片缓存(二级缓存:LruMemoryCache 和 File)
* 图片加载(ImageLoader, ImageBlurLoader):线程池实现而非AsyncTask
* 通过MD5加密保存文件 

## 描述

瀑布流，高斯模糊，下拉刷新上拉加载，图片异步加载，高斯模糊异步处理，点击item即可显示原来的图片。坚决不卡 UI，坚决异步，多线程处理，图片缓存到 LruMemoryCache，同时使用 MD5 加密写到 SD卡中，最最关键的是：没有OOM！！！！！上线已半年之久，从来木有OOM过！！！

注：必须采用源码中所用的线程池来处理，不能使用 Android 自带的 AsyncTask ，因为 AsyncTask 会有限制，超过一定数量（好像是150+，具体忘记了）一定会crash，也许是我之前用 AsyncTask 时写的不够完美，但是我无法找到crash原因。

注2：不能使用 StaggeredGridView 的 itemclick 方法，StaggeredGridView的源码就是有问题的，itemclick的时候会造成 item 不可控的移位，解决办法是，所有的 click 事件都去对应的控件处理，如果是整个 item，那就设置整个item为一个view 对这个view进行 click 操作。

注3：StaggeredGridView上拉加载的时候，只刷新最后一个 view， 所以不会有界面一闪的情况。当点击指定 item 进行解锁的时候会刷新所有可见view，原因是 如果刷新点击的item，也会造成不可预料的问题。

注4：demo 中下载过慢的问题是因为我取的图片都是比较大的（300k+），建议和服务端协商好，控制好大小（100K以内）这么小的图片本来就不应该用这么大图片的。点击解锁一闪的问题也是这个原因(已修复)



## describe

Waterfalls flow, Gaussian blur, pull to refresh and release to refresh, pictures asynchronous loading, Gaussian blur asynchronous operation, just click the item to display  the original picture, never to slow UI, All time-consuming operations are asynchronous operation, multi-threaded operation, image cache to LruMemoryCache, while using MD5 encryption writes SD card, the most critical is: no OOM!!!! The online has been for six months, had never OOM wood!!

Note 1: The source code must use the thread pool used to deal with, you can not use Android comes AsyncTask, because AsyncTask be limited, more than a certain number (like 150) will crash, probably before I use AsyncTask When writing is not perfect, but I can not find the reason crash.

Note 2: You can not use the itemclick StaggeredGridView method, StaggeredGridView source is questionable, itemclick when the item will cause uncontrollable shift, the solution is to go all the click event corresponding control processing, if the entire item, Then set the entire item to perform a view to this view click operation.
Note 3: When the StaggeredGridView release to refresh, only refreshes the last item view, so there is no flash in the case of the interface. When you click the item to unlock the specified time will refresh all visible item view, because if refresh clicked item, can also cause unexpected problems.

Note 4: demo download problem is too slow because I take the pictures are relatively large (300k), advice to services negotiated end, good control of size (100K or less) so small pictures should not have spent so much picture. Click to unlock flash problem is for this reason

Note 5: Image Source: http: //www.lofter.com any problems, immediately delete


## Preview

[Download Demo](https://github.com/MrFuFuFu/MrFuBlurView/releases/download/1.0.0/MrFuBlurView.apk)

![preview1](http://ww3.sinaimg.cn/large/005tyPhMgw1eqke5wvqw6g30a00hsgw8.gif)


## Open source projects

* [StaggeredGridView](https://github.com/maurycyw/StaggeredGridView)

* [ProgressBarCircularIndeterminate](https://github.com/navasmdc/MaterialDesignLibrary)

## More about me

* [MrFu-傅圆的个人博客](http://mrfufufu.github.io/)

License
============

    Copyright 2015 MrFu

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.