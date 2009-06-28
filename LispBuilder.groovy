class LispBuilder {
  private LispList readBuffer

  def readStart() {
    readBuffer = null
  }

  def readResult() {
    readBuffer
  }

  def build(Closure c) {
    readStart()
    c.delegate = this
    c.resolveStrategy = Closure.DELEGATE_FIRST;
    c.call()
    return readResult()
  }

  def invokeMethod(String m, args) {
    try {
      if (m != '$') {
        getProperty(m)
      }
      def elem = args[0]
      if (elem instanceof Closure) {
        LispBuilder reader = new LispBuilder()
        elem = reader.build(elem)
      }
      if (elem != null) {
        if (readBuffer == null) {
          readBuffer = [elem, null] as Cons
        }
        else {
          readBuffer.append_(elem)
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

  def getProperty(String p) {
    try {
      def value = p
      if (p.startsWith('$')) {
        value = Integer.parseInt(p.substring(1,p.size()))
      }
      if (readBuffer == null) {
        readBuffer = [value] as Cons
      }
      else {
        readBuffer.append_(value)
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

}