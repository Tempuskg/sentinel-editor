package com.sentinel.editor.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * SentinelEditor Color Palette
 * 
 * Design Rationale:
 * - Dark theme by default (developer tools)
 * - Accent colors for GitHub actions (commit colors from GitHub API)
 * - Syntax highlighting via Markwon
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */

val Primary = Color(0xFF1F6FEB) // GitHub blue
val PrimaryDark = Color(0xFF2C66D3)
val PrimaryLight = Color(0xFF4885ED)

val Secondary = Color(0xFF3FBCE4) // Cyan accent
val SecondaryDark = Color(0xFF1EA3A5)

val AccentCommit = Color(0xFF44BD32) // Green (commit)
val AccentPull = Color(0xFF0D93F3) // Blue (PR)
val AccentIssue = Color(0xFFEB4D4B) // Red (issue)
val AccentDraft = Color(0xFFFF9900) // Orange (draft)

val Background = Color(0xFF151618) // Very dark (GitHub dark theme)
val BackgroundSurface = Color(0xFF1C1D1F)
val BackgroundElevated = Color(0xFF242527)

val SurfaceHigh = Color(0xFF3C3F41)
val SurfaceMedium = Color(0xFF4E5153)
val SurfaceLow = Color(0xFF1A1D21)

val OnBackground = Color(0xFFECECEC) // High contrast foreground
val OnBackgroundMedium = Color(0xFFE6E6E6)
val OnBackgroundLow = Color(0xFFB0B3B8)

val Error = Color(0xFFEB4D4B)
val ErrorContainer = Color(0xFF9C2D2D)
val ErrorText = Color(0xFFFFAEAEB9)

val Success = Color(0xFF3FB436)
val Warning = Color(0xFFFFAE12)
val Info = Color(0xFF0D93F3)

val Outline = Color(0xFF4E5153)
val OutlineVariant = Color(0xFF3C3F41)

// Syntax highlighting colors (dark theme)
val SyntaxKeyword = Color(0xFF569CD6)
val SyntaxString = Color(0xFFCE9178)
val SyntaxNumber = Color(0xFFB58900)
val SyntaxComment = Color(0xFF6A9955)
val SyntaxClass = Color(0xFF4EC9B0)
val SyntaxFunction = Color(0xFFDCDCAA)
val SyntaxType = Color(0xFF4EC9B0)
