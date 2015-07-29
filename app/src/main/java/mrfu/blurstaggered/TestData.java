package mrfu.blurstaggered;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Mr.傅
 * 2015-3-26 下午9:21:57
 */
public class TestData {
	private static final String[] urls = new String[]{
		"http://i.qichuang.com/01150522bbb76500b06f497bae1ace9677b0dfb5.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150526ec80b3ec9a9e425784b149fcc1c78ebf.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011506013d2c71af450348ddbde44ee464ed703e.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504307c44f0bf9b7b4175aca124c2cd114903.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150413138648b7673d45eb9e3d18f13814ea49.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504100b21213949df4daa8bbe03340e924232.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115040176a71ef9351747e0973fb0cc8efeafe2.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503308902e4a406814e3b84091a4d8e22f34d.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115032997c599a2fb264848a6d30881759c36f3.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503285193bef9427a4cefa6bfe53dc75a6007.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150326ce704d90f7314ab3a008e50ac4d84eca.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115041435a2869c08c54dc3b86d5d362883395a.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115041332742c729eb74b45aca213ddd7d1a6f7.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150409b82e19278b0e44279afb4d4ec7501267.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150408c5242f99a30042c3a9b9f4bc2f738bf3.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504077a49638c36164dc08c8158ee5db77884.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503301fcaaba459e9429d83109bb4444a062b.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503275d7d1123c17c40de9eb2add6912aa335.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503248670f99478e343809dac41bdedeb377c.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503233440c61596ce465d823e9a878c6ae125.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503200c4bd6b87fd7407fa86b77dfe7a5d52f.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115051842e5a4e6db6044a29e1fd6f34f95cff3.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150517027ea87f20f64b949925ea2f2d83b875.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150514a22f144d1e19403c872ca26604fa035a.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115051312cb3daad1904f6f999231a1eb965f3b.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011505110ac1943bd98e48a9ac556be918aafe05.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150509e7422fe13f6d4f058ef42ff8261428a0.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150508e41c677abac0444b99928cc69a81eca6.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115050759c4a9c8d87c478c8c34447bdf1c3652.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150506f8fe57b9495644feb26f663c60fdb65c.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011505057dd8ce5e669e4c6b825dfec09201ba4e.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150504d61e2b52851649478c2145a7ef9091b5.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115021325174db617004933a3d1223c535d9b73.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150210ac0c18158e19454cacc9cd418c33d215.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150522bbb76500b06f497bae1ace9677b0dfb5.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150526ec80b3ec9a9e425784b149fcc1c78ebf.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011506013d2c71af450348ddbde44ee464ed703e.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504307c44f0bf9b7b4175aca124c2cd114903.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150413138648b7673d45eb9e3d18f13814ea49.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504100b21213949df4daa8bbe03340e924232.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150413138648b7673d45eb9e3d18f13814ea49.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011504100b21213949df4daa8bbe03340e924232.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115040176a71ef9351747e0973fb0cc8efeafe2.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503308902e4a406814e3b84091a4d8e22f34d.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115032997c599a2fb264848a6d30881759c36f3.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011503285193bef9427a4cefa6bfe53dc75a6007.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150326ce704d90f7314ab3a008e50ac4d84eca.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115041435a2869c08c54dc3b86d5d362883395a.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115041332742c729eb74b45aca213ddd7d1a6f7.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150409b82e19278b0e44279afb4d4ec7501267.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115051312cb3daad1904f6f999231a1eb965f3b.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011505110ac1943bd98e48a9ac556be918aafe05.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150509e7422fe13f6d4f058ef42ff8261428a0.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150508e41c677abac0444b99928cc69a81eca6.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115050759c4a9c8d87c478c8c34447bdf1c3652.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150506f8fe57b9495644feb26f663c60fdb65c.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/011505057dd8ce5e669e4c6b825dfec09201ba4e.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150504d61e2b52851649478c2145a7ef9091b5.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/0115021325174db617004933a3d1223c535d9b73.png@99q_1c_1e_250h_250w.png",
		"http://i.qichuang.com/01150210ac0c18158e19454cacc9cd418c33d215.png@99q_1c_1e_250h_250w.png",
	};

	public static List<DataModel> getPullDownDataModels(){
		List<DataModel> list = new ArrayList<DataModel>();
		for (int i = 0; i < 30; i++) {
			DataModel model = new DataModel();
			model.setId(String.valueOf(i));
			model.setIsLocked("YES");
			model.setImageUrl(urls[i]);
			list.add(model);
		}
		return list;
	}
	
	public static List<DataModel> getPullUpDataModels(){
		List<DataModel> list = new ArrayList<DataModel>();
		for (int i = 30; i < 60; i++) {
			DataModel model = new DataModel();
			model.setId(String.valueOf(i));
			model.setIsLocked("YES");
			model.setImageUrl(urls[i]);
			list.add(model);
		}
		return list;
	}



	public static List<DataModel> getTestGanhuoData() {
		TestData.HttpUtils httpUtils = new TestData.HttpUtils();
		Calendar calendar = Calendar.getInstance();
		Date today = new Date();
		calendar.set(2015, 6, 2);
		Date thatDay = calendar.getTime();

		while (thatDay.compareTo(today) <= 0) {
			String dateString = TestData.toDate(thatDay);
			thatDay = TestData.getNextdayDate(thatDay);

			String httpContent = httpUtils.download("http://gank.io/" + dateString);
			int s0 = httpContent.indexOf("<img");
			if (s0 == -1) {
				continue;
			}

			int s1 = httpContent.indexOf("src=\"", s0) + "src=\"".length();
			int e1 = httpContent.indexOf("\"", s1);
			urlLists.add(httpContent.substring(s1, e1));
		}

		List<DataModel> list = new ArrayList<DataModel>();
		for (int i = 0; i < urlLists.size(); i++) {
			DataModel model = new DataModel();
			model.setId(String.valueOf(i));
			model.setIsLocked("YES");
			model.setImageUrl(urlLists.get(i));
			list.add(model);
		}

		return list;
	}
	public static List<String> urlLists = new ArrayList<>();




	public static ArrayList<String> getGanHuoData(){
		TaskUtils.executeAsyncTask(
				new AsyncTask<Object, Object, Boolean>() {
					@Override
					protected Boolean doInBackground(Object... params) {
						HttpUtils httpUtils = new HttpUtils();
						Calendar calendar = Calendar.getInstance();
						Date today = new Date();
						calendar.set(2015, 6, 2);
						Date thatDay = calendar.getTime();

						while (thatDay.compareTo(today) <= 0) {
							String dateString = toDate(thatDay);
							thatDay = getNextdayDate(thatDay);

							publishProgress(dateString);

							String httpContent = httpUtils.download("http://gank.io/" + dateString);
							int s0 = httpContent.indexOf("<img");

							int s1 = httpContent.indexOf("src=\"", s0) + "src=\"".length();
							int e1 = httpContent.indexOf("\"", s1);
							urlLists.add(httpContent.substring(s1, e1));
						}
						return false;
					}

					@Override
					protected void onProgressUpdate(Object... values) {
						super.onProgressUpdate(values);
					}

					@Override
					protected void onPostExecute(Boolean o) {
						super.onPostExecute(o);
					}
				}
		);
		return null;
	}


	public static class TaskUtils {

		@SafeVarargs
		public static <Params, Progress, Result> void executeAsyncTask(
				AsyncTask<Params, Progress, Result> task, Params... params) {
			if (Build.VERSION.SDK_INT >= 11) {
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
			} else {
				task.execute(params);
			}
		}
	}

	public static class HttpUtils {

		private URL url = null;

		public String download(String urlStr) {
			StringBuffer sb = new StringBuffer();
			String line = null;
			BufferedReader buffer = null;
			try {
				url = new URL(urlStr);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				buffer = new BufferedReader(
						new InputStreamReader(
								urlConn.getInputStream()
						)
				);
				while ((line = buffer.readLine()) != null) {
					sb.append(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (buffer != null) {
						buffer.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
	}


	public static String toDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return dateFormat.format(date);
	}

	public static Date getLastdayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	public static Date getNextdayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

}
