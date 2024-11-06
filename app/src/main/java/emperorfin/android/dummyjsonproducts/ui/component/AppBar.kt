package emperorfin.android.dummyjsonproducts.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import emperorfin.android.dummyjsonproducts.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String?,
    onBackPress: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.height(70.dp),
        title = {
            Box {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopCenter),
                    text = title ?: "",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        navigationIcon = {
            onBackPress?.let {
                IconButton(onClick = { it() }) {
                    Box {
                        Image(
                            imageVector = Icons.Filled.ArrowBack,
                            colorFilter = ColorFilter.tint(Color.White),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40
        )
    )
}