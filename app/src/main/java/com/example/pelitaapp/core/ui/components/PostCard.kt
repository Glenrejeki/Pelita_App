package com.example.pelitaapp.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pelitaapp.core.data.model.Post
import com.example.pelitaapp.core.data.model.PostType
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    onClickPost: ((Post) -> Unit)? = null,
    onClickLike: ((Post) -> Unit)? = null,
    onClickComment: ((Post) -> Unit)? = null,
    onClickRepost: ((Post) -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onClickPost != null) {
                onClickPost?.invoke(post)
            },
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "@${post.author.username}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = formatTime(post.createdAt.toString()),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // label untuk repost/quote
                when (post.postType) {
                    PostType.REPOST -> Text(
                        text = "Repost",
                        style = MaterialTheme.typography.labelMedium
                    )
                    PostType.QUOTE -> Text(
                        text = "Quote",
                        style = MaterialTheme.typography.labelMedium
                    )
                    else -> Unit
                }
            }

            Spacer(Modifier.size(10.dp))

            // Content utama
            val contentText = post.content ?: ""
            if (contentText.isNotBlank()) {
                Text(
                    text = contentText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Jika repost atau quote => tampilkan original post ringkas
            val original = post.originalPost
            if (post.postType != PostType.ORIGINAL && original != null) {
                Spacer(Modifier.size(10.dp))
                OriginalPostPreview(original)
            }

            Spacer(Modifier.size(8.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onClickLike?.invoke(post) },
                        enabled = onClickLike != null
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like"
                        )
                    }
                    Text(
                        text = post.likeCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onClickComment?.invoke(post) },
                        enabled = onClickComment != null
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment"
                        )
                    }
                    Text(
                        text = post.commentCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onClickRepost?.invoke(post) },
                        enabled = onClickRepost != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repost"
                        )
                    }
                    Text(
                        text = post.repostCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun OriginalPostPreview(original: Post) {
    Card(
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "@${original.author.username}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(6.dp))
            Text(
                text = original.content ?: "",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatTime(iso: String): String {
    return try {
        val instant = java.time.Instant.parse(iso)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (_: Exception) {
        iso
    }
}
