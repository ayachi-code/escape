package tetris.logic
import ddf.minim.{AudioPlayer, Minim}

class Audio(var url: String, minim: Minim) {
  private val audio: AudioPlayer = minim.loadFile(url)

  def play(): Unit = {
    audio.play()
    audio.rewind()
  }

  def pause(): Unit = audio.pause()

  def loop(): Unit = audio.loop()

}
