package com.mrfu.blurview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.傅
 * 2015-3-26 下午9:21:57
 */
public class TestData {
	private static final String[] urls = new String[]{
		"http://imglf1.ph.126.net/chesKSW663REI97X2Z6aEw==/6630734613675789868.jpg",
		"http://imglf1.ph.126.net/3AD_C9T0S2B3OEq2Mvq-YA==/6630228838326863842.jpg",
		"http://imglf2.ph.126.net/WVWyZGpj2QhPd3TyBTpyGA==/6630251928071047538.jpg",
		"http://imglf0.ph.126.net/Y_Bd_vemWWTIjb2ERxFTEQ==/6630103494001298067.jpg",
		"http://imglf0.ph.126.net/w9BTVYXmR_DLOXyNnDpUDg==/6630209047117563912.jpg",
		"http://imglf2.ph.126.net/ojOVY2f0xcPZrjGRlhPr3g==/6630057314512933922.jpg",
		"http://imglf0.ph.126.net/0jRZEMafWiOa5s8fOEF30g==/6630286012931509200.jpg",
		"http://imglf0.ph.126.net/Xv2ROvwkfRjuPXfBfQuyUQ==/843299030325733278.jpg",
		"http://imglf0.ph.126.net/uWDlVPyXMXBsjBbc34ky_g==/6630799484861807643.jpg",
		"http://imglf0.ph.126.net/A9uT_ga3VMbHyWF-Tx6VTw==/6630712623443185368.jpg",
		"http://imglf2.ph.126.net/6ecdEb6sHFy_KS7vmzhzZQ==/6630069409140837935.jpg",
		"http://imglf2.ph.126.net/NXt0GSDQLuJQSimPkuqYjw==/6630514711350087341.jpg",
		"http://imglf2.ph.126.net/lphfR1pkrIbxTYajFWQMIA==/6630585080094392252.jpg",
		"http://imglf0.ph.126.net/_G3djArnfs0JAx5nZhaU-Q==/6630644453722294323.jpg",
		"http://imglf2.ph.126.net/El-VNafpu6XPkXwVtQiuog==/6630804982419957371.jpg",
		"http://imglf2.ph.126.net/KUDi_HUSPtEEaZQS3EUvEQ==/6630334391443076603.jpg",
		"http://imglf1.ph.126.net/zKEhGFG8Dzn-9WkYAa9Efw==/981221768831355220.jpg",
		"http://imglf1.ph.126.net/DrlYHV75nCJQ_nqxzHYizA==/6630447641140801849.jpg",
		"http://imglf0.ph.126.net/6DVq8IydLl8SUnOklswq2Q==/6619398648793950870.jpg",
		"http://imglf0.ph.126.net/AGylCFBkmsRxwikyDzdRGw==/6630633458606029362.jpg",
		"http://imglf1.ph.126.net/rz8XSZ0qE5AbQigK5K4ARQ==/3805260210251712088.jpg",
		"http://imglf1.ph.126.net/A_Qvpe9TYpPPc3dqgyHKdg==/6630633458606029369.jpg",
		"http://imglf1.ph.126.net/TPQamtvljGCLTisnQCS6hA==/2435321498518631992.jpg",
		"http://imglf1.ph.126.net/SF30XT0i5KH5na6Elfp04g==/6630884147257162579.jpg",
		"http://imglf0.ph.126.net/r50Ki1_D7LEAEGQU_TOYGg==/6630779693652510096.jpg",
		"http://imglf1.ph.126.net/m6X0GLdZ-M5qIs6cvhuOtw==/6630755504396698300.jpg",
		"http://imglf2.ph.126.net/sJ_zhm9Ws--phawUsufCQw==/6630703827350192027.jpg",
		"http://imglf1.ph.126.net/vi6c-SlzXUn4fNkDITDjGQ==/6608875223004427930.jpg",
		"http://imglf1.ph.126.net/18aC8qH7xf3mfy75lTUS8w==/1175158027785075106.jpg",
		"http://imglf1.ph.126.net/g92BjDMhyRKTDeMaUCDk0A==/6619242518142804511.jpg",
		"http://imglf2.ph.126.net/-UlDpFs63JeHLFgKfk4OMQ==/2394507626895627947.jpg",
		"http://imglf1.ph.126.net/qytriQ_Io-O7gdqdLKjOdA==/2388033702431279677.jpg",
		"http://imglf1.ph.126.net/wpbGs6v8-UUXyy_djNoojw==/6619433833166038036.jpg",
		"http://imglf1.ph.126.net/wpbGs6v8-UUXyy_djNoojw==/6619433833166038036.jpg",
		"http://imglf0.ph.126.net/1StnKn2eajCBOjIHaOZ1lA==/1311110441618586609.jpg",
		"http://imglf1.ph.126.net/BrRmrEazC_2IHxSdLRfdoA==/6619388753189296549.jpg",
		"http://imglf0.ph.126.net/tAYhvRlRj62vvkG1p9TQHQ==/6630831370699013202.jpg",
		"http://imglf1.ph.126.net/y30oW_aeQE6T63CTgnyybQ==/1181913427226117491.jpg",
		"http://imglf1.ph.126.net/_r7AeDet9BC0Rp9Fh171OA==/6630365177768715274.jpg",
		"http://imglf1.ph.126.net/o2odDyyYOgVpcpm_RMeqaw==/6630092498885029594.jpg",
		"http://imglf0.ph.126.net/lcpG_8VL4stA4LwJapzQUQ==/6630568587419987631.jpg",
		"http://imglf2.ph.126.net/6Nf6uX1lgQC_EnZBbZPePQ==/2749166097550944548.jpg",
		"http://imglf1.ph.126.net/-SSZ1K-go5W05rKgNzj-aA==/3360529747050676126.jpg",
		"http://imglf0.ph.126.net/A9LEHNVtdUuZTHImD9bUAw==/6619088586514912287.jpg",
		"http://imglf0.ph.126.net/xWax6zPwcw4p1WghUuzBPA==/1051871987985623079.jpg",
		"http://imglf2.ph.126.net/awQORBuScZQ_WQq0Wl9jMA==/3109454067740688605.jpg",
		"http://imglf2.ph.126.net/M00q_M8-4te8-YvCtK3-nA==/2402670401220212429.jpg",
		"http://imglf0.ph.126.net/-RfZ0Bw-rZ9jVhngRJjcXA==/6630825873140872464.jpg",
		"http://imglf0.ph.126.net/kok7lYtANizuA6_Bv05blg==/6630782992187382243.jpg",
		"http://imglf1.ph.126.net/kPY8zwr2vP6hn_zoRl8ICg==/6630444342605897386.jpg",
		"http://imglf2.ph.126.net/Pi6IzU7--W6ODNC9EkL73A==/1176283927691934050.jpg",
		"http://imglf0.ph.126.net/gb9YAs5MvIIbERQiPEhugw==/6619474515096263739.jpg",
		"http://imglf2.ph.126.net/JECRCtR_h4nzi0SLuYyx0w==/6630619164954841664.jpg",
		"http://imglf2.ph.126.net/YxDjI-Nj_90V1cU837ddTw==/6630074906698964663.jpg",
		"http://imglf0.ph.126.net/XCWo2e4gYZeDpgPSh7uveA==/67272519451921236.jpg",
		"http://imglf1.ph.126.net/vxq3dft9-gbdK6MuOllZww==/3357996472260787393.jpg",
		"http://imglf0.ph.126.net/loR2IBXGcquHNUGNQZmb-w==/6630645553233531300.jpg",
		"http://imglf1.ph.126.net/4YU4Gilkncr1hKXvWWcmZA==/6630625762024587685.jpg",
		"http://imglf2.ph.126.net/C8RmXmccsfF3oGgQQI3zYg==/6630405859698791513.jpg",
		"http://imglf0.ph.126.net/oRxcC-uK4Dn70zIzio5eoQ==/6608664116771893014.jpg",
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
}
