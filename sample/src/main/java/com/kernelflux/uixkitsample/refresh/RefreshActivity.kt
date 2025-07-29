package com.kernelflux.uixkitsample.refresh

import android.os.Bundle
import com.kernelflux.uixkit.adapter.SmartRefreshContainer
import com.kernelflux.uixkit.adapter.setupUltraAdapter
import com.kernelflux.uixkit.core.BaseActivity
import com.kernelflux.uixkitsample.R
import com.kernelflux.uixkitsample.databinding.ListItemRefresh1Binding
import com.kernelflux.uixkitsample.databinding.ListItemRefresh2Binding
import com.bumptech.glide.Glide
import com.kernelflux.uixkit.adapter.UltraSimpleAdapter
import com.kernelflux.uixkit.adapter.setupListeners
import android.util.Log
import com.kernelflux.uixkitsample.BuildConfig

class RefreshActivity : BaseActivity() {

    private var mContainer: SmartRefreshContainer? = null
    private var mAdapter: UltraSimpleAdapter<RefreshItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)

        mContainer = castViewByResId(R.id.sr_container)

        initRefreshView()

    }


    private fun initRefreshView() {
        mContainer?.apply {
            // 自动添加默认的刷新和加载更多视图
            setupDefaultHeaders()

            // 配置动画时间，让响应更快
            setAnimationDuration(200)

            // 配置适配器
            setupUltraAdapter<RefreshItem> {
                item<ListItemRefresh1Binding>(
                    isForViewType = { item, position -> item.type == 0 },
                    onBind = { binding, item, position ->
                        // 优化图片加载，减少抖动
                        Glide.with(binding.aciLogo)
                            .load(item.imgUrl)
                            .placeholder(R.drawable.placeholder_image) // 使用自定义占位图
                            .error(R.drawable.placeholder_image) // 使用自定义错误图
                            .centerCrop() // 确保裁剪方式一致
                            .dontAnimate() // 禁用 Glide 动画，减少抖动
                            .into(binding.aciLogo)
                        binding.actDesc.text = item.text
                    }
                ).item<ListItemRefresh2Binding>(
                    isForViewType = { item, position -> item.type == 1 },
                    onBind = { binding, item, position ->
                        // 优化图片加载，减少抖动
                        Glide.with(binding.aciLogo2)
                            .load(item.imgUrl)
                            .placeholder(R.drawable.placeholder_image) // 使用自定义占位图
                            .error(R.drawable.placeholder_image) // 使用自定义错误图
                            .centerCrop() // 确保裁剪方式一致
                            .dontAnimate() // 禁用 Glide 动画，减少抖动
                            .into(binding.aciLogo2)
                        binding.actDesc2.text = item.text
                    }
                )
            }.let { adapter ->
                mAdapter = adapter
            }

            // 设置监听器
            setupListeners(
                onRefresh = { loadData() },
                onLoadMore = { loadMoreData() }
            )
        }

        // 自动刷新加载初始数据
        mContainer?.postDelayed({
            mContainer?.autoRefresh()
        }, 500) // 延迟500ms后自动刷新
    }

    private fun loadData() {
        mContainer?.postDelayed({
            val mockData = listOf(
                RefreshItem(
                    id = "refresh_1",
                    type = 0,
                    text = "Description 1",
                    imgUrl = "https://cdn.pixabay.com/photo/2016/12/05/11/39/fox-1883658_1280.jpg"
                ),
                RefreshItem(
                    id = "refresh_2",
                    type = 1,
                    text = "Description 2",
                    imgUrl = "https://cdn.pixabay.com/photo/2019/12/17/06/50/squirrel-4700919_1280.jpg"
                ),
                RefreshItem(
                    id = "refresh_3",
                    type = 0,
                    text = "Description 3",
                    imgUrl = "https://cdn.pixabay.com/photo/2018/07/14/17/46/raccoon-3538081_1280.jpg"
                ),
                RefreshItem(
                    id = "refresh_4",
                    type = 1,
                    text = "Description 4",
                    imgUrl = "https://cdn.pixabay.com/photo/2016/11/18/16/33/koala-1835689_1280.jpg"
                )
            )

            mAdapter?.submitList(mockData)
            mContainer?.finishRefresh()
        }, 500)
    }

    private fun loadMoreData() {
        mContainer?.postDelayed({
            val currentList = mAdapter?.currentList?.toMutableList() ?: mutableListOf()
            val newItems = listOf(
                RefreshItem(
                    id = "loadmore_${System.currentTimeMillis()}_1",
                    type = 0,
                    text = "More Description 1",
                    imgUrl = "https://cdn.pixabay.com/photo/2016/12/05/11/39/fox-1883658_1280.jpg"
                ),
                RefreshItem(
                    id = "loadmore_${System.currentTimeMillis()}_2",
                    type = 1,
                    text = "More Description 2",
                    imgUrl = "https://cdn.pixabay.com/photo/2019/12/17/06/50/squirrel-4700919_1280.jpg"
                ),
                RefreshItem(
                    id = "loadmore_${System.currentTimeMillis()}_3",
                    type = 0,
                    text = "More Description 3",
                    imgUrl = "https://cdn.pixabay.com/photo/2018/07/14/17/46/raccoon-3538081_1280.jpg"
                ),
                RefreshItem(
                    id = "loadmore_${System.currentTimeMillis()}_4",
                    type = 1,
                    text = "More Description 4",
                    imgUrl = "https://cdn.pixabay.com/photo/2016/11/18/16/33/koala-1835689_1280.jpg"
                )
            )

            // 优化数据更新，减少抖动
            updateDataSmoothly(currentList, newItems)

            mContainer?.finishLoadMore()
        }, 500)
    }

    /**
     * 平滑更新数据，减少抖动
     */
    private fun updateDataSmoothly(currentList: MutableList<RefreshItem>, newItems: List<RefreshItem>) {
        try {
            // 延迟更新数据，让图片有时间预加载
            mAdapter?.let { adapter ->
                // 使用 DiffUtil 进行平滑更新
                val updatedList = currentList.toMutableList()
                updatedList.addAll(newItems)

                // 延迟提交数据，确保图片预加载完成
                adapter.submitList(updatedList) {
                    // 数据更新完成后的回调
                    logDebug("Data updated smoothly, new items: ${newItems.size}")
                }
            } ?: run {
                logDebug("Adapter is null, cannot update data")
            }
        } catch (e: Exception) {
            logDebug("Error updating data: ${e.message}")
            // 降级处理：直接更新数据
            mAdapter?.submitList(currentList + newItems)
        }
    }

    /**
     * 日志输出
     */
    private fun logDebug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("RefreshActivity", message)
        }
    }



}