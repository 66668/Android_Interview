# ViewPager使用细节

## 如何设置成每次只初始化当前的Fragment，其他的不 初始化()?

使用Fragment懒加载方式：

自定义一个 LazyLoadFragment 基类，利用 setUserVisibleHint 和 生命周期方法，通过对 Fragment 状态判断，进行数据加载，并将数据加载的接口提供开放出去，
供子类使用。然后在子类 Fragment 中 实现 requestData 方法即可。这里添加了一个 isDataLoaded 变量，目的是避免重复加载数据。考虑到 有时候需要刷新数据的问题，
便提供了一个用于强制刷新的参数判断。

        
        //懒加载基类
        public abstract class LazyLoadFragment extends BaseFragment {
            protected boolean isViewInitiated;
            protected boolean isDataLoaded;
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                isViewInitiated = true;
                prepareRequestData();
            }
            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                prepareRequestData();
            }
            public abstract void requestData();
            public boolean prepareRequestData() {
                return prepareRequestData(false);
            }
            public boolean prepareRequestData(boolean forceUpdate) {
                if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded ||
        forceUpdate)) {
                    requestData();
                    isDataLoaded = true;
                    return true;
        }
                return false;
            }
        }