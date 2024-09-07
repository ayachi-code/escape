package escape.logic
import ddf.minim.{AudioPlayer, Minim}

class Audio(var url: String, minim: Minim) {
  private val audio: AudioPlayer = minim.loadFile(url)

  def play(): Unit = {
    if (audio == null) return
    audio.play()
    audio.rewind()
  }

  def continue(): Unit = if (audio != null) audio.play()
  def pause(): Unit = if (audio != null) audio.pause()
  def loop(): Unit = if (audio != null) audio.loop()
  def stop() : Unit = if (audio != null) audio.close()
}
