package com.android.catchdesign.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.android.catchdesign.R
import com.android.catchdesign.utils.NetworkUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    context: Context,
    viewModel: ContentViewModel = hiltViewModel(),
    onItemClick: (title: String, content: String) -> Unit = { title, content -> }
){

    val data by viewModel.contents.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            if (isRefreshing) {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = Color.White,
                                trackColor = colorResource(R.color.home),
                                strokeWidth = 4.dp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.theme_blue)
                    )
                )
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                if ((!NetworkUtils.isNetworkAvailable(context) && data.isEmpty()) || data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.home)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!NetworkUtils.isNetworkAvailable(context)) {
                            Text(
                                text = stringResource(R.string.network_connection),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo"
                            )
                        }
                    }
                }
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.refreshContent() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = state,
                    indicator = {}
                ) {
                    if (data.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(data.size) { index ->
                                ContentItem(
                                    content = data[index],
                                    onClick = {
                                        onItemClick(
                                            data[index].title,
                                            data[index].content
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
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
                .padding(horizontal = 15.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = content.title,
                    fontSize = 18.sp,
                    color = colorResource(R.color.theme_blue)
                )
            }
            Column(
                modifier = Modifier.weight(2f)
            ){ Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = content.subtitle,
                        fontSize = 18.sp,
                        color = colorResource(R.color.sub_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        textAlign = TextAlign.End
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = colorResource(R.color.theme_blue)
                    )
                }
            }
        }
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}