package com.github.shareme.gwsviewpagertrans;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.shareme.gwsviewpagertrans.library.AccordionTransformer;
import com.github.shareme.gwsviewpagertrans.library.BackgroundToForegroundTransformer;
import com.github.shareme.gwsviewpagertrans.library.CubeInTransformer;
import com.github.shareme.gwsviewpagertrans.library.CubeOutTransformer;
import com.github.shareme.gwsviewpagertrans.library.DefaultTransformer;
import com.github.shareme.gwsviewpagertrans.library.DepthPageTransformer;
import com.github.shareme.gwsviewpagertrans.library.FlipHorizontalTransformer;
import com.github.shareme.gwsviewpagertrans.library.FlipVerticalTransformer;
import com.github.shareme.gwsviewpagertrans.library.ForegroundToBackgroundTransformer;
import com.github.shareme.gwsviewpagertrans.library.RotateDownTransformer;
import com.github.shareme.gwsviewpagertrans.library.RotateUpTransformer;
import com.github.shareme.gwsviewpagertrans.library.StackTransformer;
import com.github.shareme.gwsviewpagertrans.library.TabletTransformer;
import com.github.shareme.gwsviewpagertrans.library.ZoomInTransformer;
import com.github.shareme.gwsviewpagertrans.library.ZoomOutSlideTransformer;
import com.github.shareme.gwsviewpagertrans.library.ZoomOutTranformer;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnNavigationListener {

	private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
	private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
	private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;

	static {
		TRANSFORM_CLASSES = new ArrayList<TransformerItem>();
		TRANSFORM_CLASSES.add(new TransformerItem(DefaultTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(AccordionTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(BackgroundToForegroundTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(DepthPageTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(FlipHorizontalTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(FlipVerticalTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(RotateDownTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(RotateUpTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(StackTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(TabletTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(ZoomInTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutSlideTransformer.class));
		TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutTranformer.class));
	}

	private int mSelectedItem;
	private ViewPager mPager;
	private PageAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int selectedPage = 0;
		if (savedInstanceState != null) {
			mSelectedItem = savedInstanceState.getInt(KEY_SELECTED_CLASS);
			selectedPage = savedInstanceState.getInt(KEY_SELECTED_PAGE);
		}

		final ArrayAdapter<TransformerItem> actionBarAdapter = new ArrayAdapter<TransformerItem>(
				getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, TRANSFORM_CLASSES);

		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setListNavigationCallbacks(actionBarAdapter, this);
		}
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		}
		if (actionBar != null) {
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		}

		setContentView(R.layout.activity_main);

		mAdapter = new PageAdapter(getFragmentManager());

		mPager = (ViewPager) findViewById(R.id.container);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(selectedPage);

		actionBar.setSelectedNavigationItem(mSelectedItem);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		mSelectedItem = position;
		try {
			mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(position).clazz.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(KEY_SELECTED_CLASS, mSelectedItem);
		outState.putInt(KEY_SELECTED_PAGE, mPager.getCurrentItem());
	}

	public static class PlaceholderFragment extends Fragment {

		private static final String EXTRA_POSITION = "EXTRA_POSITION";
		private static final int[] COLORS = new int[] { 0xFF33B5E5, 0xFFAA66CC, 0xFF99CC00, 0xFFFFBB33, 0xFFFF4444 };

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final int position = getArguments().getInt(EXTRA_POSITION);
			final TextView textViewPosition = (TextView) inflater.inflate(R.layout.fragment_main, container, false);
			textViewPosition.setText(Integer.toString(position));
			textViewPosition.setBackgroundColor(COLORS[position - 1]);

			return textViewPosition;
		}

	}

	private static final class PageAdapter extends FragmentStatePagerAdapter {

		public PageAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			final Bundle bundle = new Bundle();
			bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position + 1);

			final PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setArguments(bundle);

			return fragment;
		}

		@Override
		public int getCount() {
			return 5;
		}

	}

	private static final class TransformerItem {

		final String title;
		final Class<? extends PageTransformer> clazz;

		public TransformerItem(Class<? extends PageTransformer> clazz) {
			this.clazz = clazz;
			title = clazz.getSimpleName();
		}

		@Override
		public String toString() {
			return title;
		}

	}

}
