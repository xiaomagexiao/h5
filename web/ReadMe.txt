h5简单介绍：
1、该实现方式是基于js方法的  prompt(result); 传递json信息给app的webview，app
      根据事先定义好的内容进行处理，返回json信息，调用回调函数。
      
2、浏览器调试的时候可以模拟返回json信息数据达到调试效果。

3、该方式只针对数据及请求的交互，不涉及页面效果的控制。

4、根据需求以后可以定义更多的类型来满足业务需求，现在支持的有：
	openview：调整到一个新的网页页面
	alert：对应anroid的toast，iOS的MB提示
	select：调用原生的选择框
	confirm：确认取消
	opencourse：进入课程学习
   以上这些具体的用法参考index.html

5、mobileheader_new.html 是一个正在使用的项目的例子。

   
   
