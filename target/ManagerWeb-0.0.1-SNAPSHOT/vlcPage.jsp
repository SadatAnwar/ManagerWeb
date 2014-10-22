<html>
<head><title>Demo of VLC mozilla plugin</title></head>

<body>

<embed type="application/x-vlc-plugin"
         name="video1"
         autoplay="yes" loop="yes" width="1280" height="1024"
         target="file:///C:\Users\anwar\Desktop\Gravity.mp4" />
<br />
  <a href="javascript:;" onclick='document.video1.play()'>Play video1</a>
  <a href="javascript:;" onclick='document.video1.pause()'>Pause video1</a>
  <a href="javascript:;" onclick='document.video1.stop()'>Stop video1</a>
  <a href="javascript:;" onclick='document.video1.fullscreen()'>Fullscreen</a>

</body>
</html>