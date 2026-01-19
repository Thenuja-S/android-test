package com.android.catchdesign.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.catchdesign.domain.models.ContentItem
import android.util.Log
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.android.catchdesign.R
import com.android.catchdesign.utils.NetworkUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    context: Context,
    viewModel: ContentViewModel = hiltViewModel(),
    modifier: Modifier,
    onItemClick: (title: String, content: String) -> Unit = { title, content -> }
){

    val data by viewModel.contents.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Column {
                if (isRefreshing) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(60.dp),
                                        color = Color.White,
                                        trackColor = colorResource(R.color.home),
                                        strokeWidth = 6.dp,

                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colorResource(R.color.theme_blue)
                        )
                    )
                }
            }
        },
    ) { paddingValues ->
        Column (
            modifier.fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refreshContent() },
                modifier  .fillMaxSize()
                    .padding(paddingValues),
                state = state,
                indicator = {}
            ) {
                if (!NetworkUtils.isNetworkAvailable(context) && data.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.home)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.network_connection),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.align(Alignment.Center).padding(start = 4.dp),
                            color = Color.White
                        )

                    }
                }
                else if (data.isEmpty()) {
                    Box(
                        modifier.fillMaxSize()
                            .background(colorResource(R.color.home)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                        )}
                }
                else {
                    LazyColumn(
                        modifier.fillMaxSize()
                                .windowInsetsPadding(WindowInsets.statusBars)
                        ) {
                            items(data.size) { index ->
                                Log.i("DesignUI", "ListScreen: ${data[index]}")
                                ContentItem(
                                    content = data[index],
                                    onClick = {
                                        onItemClick(
                                            data[index].title,
                                            data[index].content
                                        )
                            }) }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentItem(content: ContentItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = content.title,
                fontSize = 20.sp,
                color = Color.Black
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = content.subtitle,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Black
                )
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
    }
}