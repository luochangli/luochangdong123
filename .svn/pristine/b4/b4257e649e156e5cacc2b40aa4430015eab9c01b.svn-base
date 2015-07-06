package com.weidi.activity;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.weidi.R;
import com.weidi.common.CircularImage;
import com.weidi.common.base.BaseActivity;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.Util;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.wheel.OnWheelChangedListener;
import com.weidi.view.wheel.WheelView;
import com.weidi.view.wheel.adapters.ArrayWheelAdapter;
import com.weidi.view.wheel.model.CityModel;
import com.weidi.view.wheel.model.DistrictModel;
import com.weidi.view.wheel.model.ProvinceModel;
import com.weidi.view.wheel.service.XmlParserHandler;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-1 上午11:49:58
 * @Description 1.0
 */
public class MeEditActivity extends BaseActivity implements
		OnWheelChangedListener, OnClickListener {
	private static String TAG = "MeEditActivity";
	
	private LinearLayout headLayout, nameLayout, emailLayout, phoneLayout,
			pwdLayout, sexLayout, signLayout, adrLayout, changeLayout,
			changeAdrLayout;
	private TextView usernameView, nameView, emailView, phoneView, sexView,
			signView, changeNameView, adrView; // nickView,
	private ScrollView scrollView;
	private EditText changeText;
	private RadioGroup sexGroup;
	private RadioButton manRadio, womanRadio;
	// private CheckBox shakeBtn, soundBtn,shareBtn;
	private CircularImage headView;
	Button exitBtn, subBtn, sureBtn, cancelBtn;
	private String field;
	private TextView changeView;
	private WheelView mViewProvince, mViewCity, mViewDistrict;

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_me);
		headLayout = (LinearLayout) findViewById(R.id.headLayout);
		nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
		emailLayout = (LinearLayout) findViewById(R.id.emailLayout);
		phoneLayout = (LinearLayout) findViewById(R.id.phoneLayout);
		pwdLayout = (LinearLayout) findViewById(R.id.pwdLayout);
		sexLayout = (LinearLayout) findViewById(R.id.sexLayout);
		signLayout = (LinearLayout) findViewById(R.id.signLayout);
		adrLayout = (LinearLayout) findViewById(R.id.adrLayout);
		changeLayout = (LinearLayout) findViewById(R.id.changeLayout);
		changeAdrLayout = (LinearLayout) findViewById(R.id.changeAdrLayout);

		usernameView = (TextView) findViewById(R.id.usernameView);
		nameView = (TextView) findViewById(R.id.nameView);
		emailView = (TextView) findViewById(R.id.emailView);
		phoneView = (TextView) findViewById(R.id.phoneView);
		sexView = (TextView) findViewById(R.id.sexView);
		signView = (TextView) findViewById(R.id.signView);
		changeNameView = (TextView) findViewById(R.id.changeNameView);
		adrView = (TextView) findViewById(R.id.adrView);

		scrollView = (ScrollView) findViewById(R.id.scrollView);
		changeText = (EditText) findViewById(R.id.changeText);
		sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
		manRadio = (RadioButton) findViewById(R.id.manRadio);
		womanRadio = (RadioButton) findViewById(R.id.womanRadio);

		headView = (CircularImage) findViewById(R.id.headView);
		exitBtn = (Button) findViewById(R.id.exitBtn);
		subBtn = (Button) findViewById(R.id.subBtn);
		sureBtn = (Button) findViewById(R.id.sureBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);

		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);

		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);
		setUpData();
		// if (Constants.USER_NAME!=null) {
		// ImgConfig.showHeadImg(Constants.USER_NAME,headView);
		// }
		if (Const.loginUser != null) {
			if (Const.USER_NAME != null) {
				usernameView.setText(Const.USER_NAME);
			}
			if (Const.loginUser.getNickname() != null) {
				nameView.setText(Const.loginUser.getNickname());
			}
			if (Const.loginUser.getEmail() != null) {
				emailView.setText(Const.loginUser.getEmail());
			}
			if (Const.loginUser.getMobile() != null) {
				phoneView.setText(Const.loginUser.getMobile());
			}
			if (Const.loginUser.getSex() != null) {
				sexView.setText(Const.loginUser.getSex());
			}
			if (Const.loginUser.getIntro() != null) {
				signView.setText(Const.loginUser.getIntro());
			}
			if (Const.loginUser.getBitmap() != null) {
				headView.setImageBitmap(Const.loginUser.getBitmap());
			}
			if (Const.loginUser.getAdr() != null) {
				adrView.setText(Const.loginUser.getAdr());
			}
		}
		sexGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == manRadio.getId()) {
					changeText.setText("男");
				} else {
					changeText.setText("女");
				}
			}
		});

	}

	@Override
	protected void setListener() {
		subBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		subBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		headView.setOnClickListener(this);
		nameLayout.setOnClickListener(this);
		emailLayout.setOnClickListener(this);
		phoneLayout.setOnClickListener(this);
		pwdLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		adrLayout.setOnClickListener(this);
		signLayout.setOnClickListener(this);

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
			case PicSrcPickerActivity.CROP:

				changeHead(data.getStringExtra("imgPath"));

				break;

			default:
				break;
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sureBtn:
			showSelectedResult();
			break;

		case R.id.exitBtn:

			break;

		case R.id.subBtn:
			// 提交修改
			String cText = changeText.getEditableText().toString();
			if (field.equals("mobile")
					&& !Util.getInstance().isMobileNumber(cText)) {

				ToastUtil.showShortToast(mApp, "不是手机号码");
			} else if (field.equals("email")
					&& !Util.getInstance().isEmail(cText)) {

				ToastUtil.showShortToast(mApp, "不是邮箱");
			} else {
				changeView.setText(changeText.getEditableText().toString());
				Const.loginUser.getvCard().setField(field,
						changeText.getEditableText().toString());
				XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
				changeLayout.setVisibility(View.GONE);
				changeText.setText("");
			}
			break;

		case R.id.cancelBtn:
			changeLayout.setVisibility(View.GONE);
			changeText.setText("");
			break;

		case R.id.headView:
			Intent intent = new Intent();
			CropImageActivity.isAutoSend = false;
			intent.setClass(this, PicSrcPickerActivity.class);
			startActivityForResult(intent, PicSrcPickerActivity.CROP);
			break;
	
		case R.id.nameLayout:
			showChangelayout("修改昵称", "nickName", nameView);
			break;
		case R.id.emailLayout:
			showChangelayout("修改邮箱", "email", emailView);
			break;
		case R.id.phoneLayout:
			showChangelayout("修改手机号码", "mobile", phoneView);
			break;
		case R.id.pwdLayout:
			startActivity(new Intent(this, ChangePwdActivity.class));
			break;
		case R.id.sexLayout:
			showChangelayout("修改性别", "sex", sexView);
			break;
		case R.id.adrLayout:
			// showChangelayout("修改地区", "adr", adrView);
			changeAdrLayout.setVisibility(View.VISIBLE);

			break;
		case R.id.signLayout:
			showChangelayout("修改签名", "intro", signView);
			break;

		default:
			break;
		}
	}

	/**
	 * @param name
	 *            //显示修改XXX
	 * @param field
	 *            //修改的字段名
	 * @param fieldView
	 *            //修改的view
	 */
	public void showChangelayout(String name, String field, TextView fieldView) {
		changeLayout.setVisibility(View.VISIBLE);
		if (field.equals("sex")) {
			sexGroup.setVisibility(View.VISIBLE);
			changeText.setVisibility(View.INVISIBLE);
		} else {
			sexGroup.setVisibility(View.GONE);
			changeText.setVisibility(View.VISIBLE);
		}
		changeNameView.setText(name);
		this.field = field;
		this.changeView = fieldView;
		scrollView.fullScroll(ScrollView.FOCUS_UP);
	}

	public void changeHead(final String imgPath) {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				if (object != null) {
					headView.setImageBitmap((Bitmap) object);
				}
			}

			@Override
			protected Object load() {
				Logger.i(TAG, "图片路径："+imgPath);
				return XmppUtil.getInstance().changeImage(new File(imgPath));
			}
		};
	}

	// 以下是选地区用的。

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = this.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	private void showSelectedResult() {
		Toast.makeText(
				this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
		adrView.setText(mCurrentDistrictName);
		Const.loginUser.getvCard().setField("adr", mCurrentDistrictName);
		XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
		changeAdrLayout.setVisibility(View.GONE);
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onResume() {
		changeText.clearFocus();
		super.onResume();
	}

}
