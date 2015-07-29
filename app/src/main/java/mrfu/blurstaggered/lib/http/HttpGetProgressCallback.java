package mrfu.blurstaggered.lib.http;

public interface HttpGetProgressCallback extends HttpGetCallback{
	public void progressPublish(String url, int progress);
}
