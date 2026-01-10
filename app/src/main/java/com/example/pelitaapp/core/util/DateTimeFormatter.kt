package com.example.pelitaapp.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Helper untuk format tanggal/waktu dari Instant / ISO string.
 * Cocok untuk Supabase timestamp (ISO-8601).
 */
object PelitaDateTimeFormatter {

    private val zone: ZoneId = ZoneId.systemDefault()

    private val fullFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm").withZone(zone)

    private val dateOnlyFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(zone)

    private val timeOnlyFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm").withZone(zone)

    fun formatFull(instant: Instant): String = fullFormatter.format(instant)

    fun formatDateOnly(instant: Instant): String = dateOnlyFormatter.format(instant)

    fun formatTimeOnly(instant: Instant): String = timeOnlyFormatter.format(instant)

    /**
     * Input contoh: "2026-01-10T12:30:00Z"
     */
    fun formatIsoFull(isoInstant: String): String {
        return try {
            val instant = Instant.parse(isoInstant)
            formatFull(instant)
        } catch (_: Exception) {
            isoInstant
        }
    }

    /**
     * Untuk PostCard: bisa lempar `Instant`
     */
    fun formatForPost(instant: Instant): String = formatFull(instant)
}
