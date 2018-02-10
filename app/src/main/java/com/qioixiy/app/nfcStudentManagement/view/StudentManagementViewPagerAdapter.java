package com.qioixiy.app.nfcStudentManagement.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class StudentManagementViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<StudentManagementFragment> fragments = new ArrayList<>();
	private StudentManagementFragment currentFragment;

	public StudentManagementViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(StudentManagementFragment.newInstance(0));
		fragments.add(StudentManagementFragment.newInstance(1));
		fragments.add(StudentManagementFragment.newInstance(2));
		fragments.add(StudentManagementFragment.newInstance(3));
		fragments.add(StudentManagementFragment.newInstance(4));
	}

	@Override
	public StudentManagementFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((StudentManagementFragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public StudentManagementFragment getCurrentFragment() {
		return currentFragment;
	}
}