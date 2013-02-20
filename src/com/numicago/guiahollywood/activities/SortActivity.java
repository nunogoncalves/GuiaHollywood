package com.numicago.guiahollywood.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.UserPreferences;

public class SortActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		Button okButton = (Button) findViewById(R.id.sort_ok);
		Button cancelButton = (Button) findViewById(R.id.sort_cancel);
		
		final RadioGroup rgSort = (RadioGroup)findViewById(R.id.sort_order_by_radio_group);
		final RadioGroup rgSearch = (RadioGroup)findViewById(R.id.sort_search_by);
		
//		if (GuiaHollywoodActivity.userPreferences.getSearchBy() == UserPreferences.SEARCH_NOME_ORIGINAL) {
//			((RadioButton)findViewById(R.id.sort_search_nome_original)).setChecked(true);	
//		} else if (GuiaHollywoodActivity.userPreferences.getSearchBy() == UserPreferences.SEARCH_NOME_LOCAL) {
//			((RadioButton)findViewById(R.id.sort_search_nome_pt)).setChecked(true);	
//		}
//		
//		if (GuiaHollywoodActivity.userPreferences.getSortBy() == UserPreferences.SORT_COUNT) {
//			((RadioButton)findViewById(R.id.sort_order_by_count)).setChecked(true);	
//		} else if (GuiaHollywoodActivity.userPreferences.getSortBy() == UserPreferences.SORT_ID) {
//			((RadioButton)findViewById(R.id.sort_order_by_input_order)).setChecked(true);	
//		} else if (GuiaHollywoodActivity.userPreferences.getSortBy() == UserPreferences.SORT_LOCAL_NAME) {
//			((RadioButton)findViewById(R.id.sort_order_by_nome_pt)).setChecked(true);	
//		} else if (GuiaHollywoodActivity.userPreferences.getSortBy() == UserPreferences.SORT_ORIGINAL_NAME) {
//			((RadioButton)findViewById(R.id.sort_order_by_nome_original)).setChecked(true);	
//		}
		
		
//		rgSort.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if (checkedId == R.id.sort_order_by_count) {
//					GuiaHollywoodActivity.userPreferences.setSortBy(UserPreferences.SORT_COUNT);
//				} else if (checkedId == R.id.sort_order_by_input_order) {
//					GuiaHollywoodActivity.userPreferences.setSortBy(UserPreferences.SORT_ID);
//				} else if (checkedId == R.id.sort_order_by_nome_original) {
//					GuiaHollywoodActivity.userPreferences.setSortBy(UserPreferences.SORT_ORIGINAL_NAME);
//				} else if (checkedId == R.id.sort_order_by_nome_pt) {
//					GuiaHollywoodActivity.userPreferences.setSortBy(UserPreferences.SORT_LOCAL_NAME);
//			}
//		}});
//		
//		rgSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if (checkedId == R.id.sort_search_nome_original) {
//					GuiaHollywoodActivity.userPreferences.setSearchBy(UserPreferences.SEARCH_NOME_ORIGINAL);
//				} else if (checkedId == R.id.sort_search_nome_pt) {
//					GuiaHollywoodActivity.userPreferences.setSearchBy(UserPreferences.SEARCH_NOME_LOCAL);
//				}
//			}});
		
		okButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				GuiaHollywoodActivity.userPreferences.saveAllFields();
				finish();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
				
			}
		});
	}
}
