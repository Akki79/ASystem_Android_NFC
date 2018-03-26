package com.qioixiy.app.nfcStudentManagement.view.student;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class StudentFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<StudentFragmentCreator> fragments = new ArrayList<>();
	private StudentFragmentCreator currentFragment;

	public StudentFragmentPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(StudentFragmentCreator.create(0));
		fragments.add(StudentFragmentCreator.create(1));
		fragments.add(StudentFragmentCreator.create(2));
	}

	@Override
	public StudentFragmentCreator getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((StudentFragmentCreator) object);
			currentFragment.onChangedIndex(position);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public StudentFragmentCreator getCurrentFragment() {
		return currentFragment;
	}

	public interface FragmentPageChanged {
		void onChangedIndex(int position);
	}
}
