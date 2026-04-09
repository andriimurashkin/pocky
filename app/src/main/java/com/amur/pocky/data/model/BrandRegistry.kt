package com.amur.pocky.data.model

import androidx.compose.ui.graphics.Color
import com.amur.pocky.R

object BrandRegistry {

    val brands: List<Brand> = listOf(
        Brand(
            id = "atb",
            displayName = "ATB",
            keywords = listOf("атб", "atb"),
            logoRes = R.drawable.brand_atb,
            primaryColor = Color(0xFFCD1719),
            secondaryColor = Color(0xFF8B0000),
        ),
        Brand(
            id = "silpo",
            displayName = "Сільпо",
            keywords = listOf("сільпо", "silpo"),
            logoRes = R.drawable.brand_silpo,
            primaryColor = Color(0xFF6AB04C),
            secondaryColor = Color(0xFF4A8A2C),
        ),
        Brand(
            id = "metro",
            displayName = "Metro",
            keywords = listOf("метро", "metro", "metro cash"),
            logoRes = R.drawable.brand_metro,
            primaryColor = Color(0xFF003B7E),
            secondaryColor = Color(0xFF00255A),
        ),
        Brand(
            id = "rozetka",
            displayName = "Rozetka",
            keywords = listOf("розетка", "rozetka"),
            logoRes = R.drawable.brand_rozetka,
            primaryColor = Color(0xFF00A046),
            secondaryColor = Color(0xFF007A33),
        ),
        Brand(
            id = "fora",
            displayName = "Фора",
            keywords = listOf("фора", "fora"),
            logoRes = R.drawable.brand_fora,
            primaryColor = Color(0xFFE31E24),
            secondaryColor = Color(0xFFB01518),
        ),
        Brand(
            id = "nova_poshta",
            displayName = "Нова Пошта",
            keywords = listOf("нова пошта", "nova poshta", "новапошта"),
            logoRes = R.drawable.brand_nova_poshta,
            primaryColor = Color(0xFFDA291C),
            secondaryColor = Color(0xFFAA1F15),
        ),
        Brand(
            id = "epicentr",
            displayName = "Епіцентр",
            keywords = listOf("епіцентр", "epicentr", "эпицентр"),
            logoRes = R.drawable.brand_epicentr,
            primaryColor = Color(0xFFF7941D),
            secondaryColor = Color(0xFFD47A10),
        ),
        Brand(
            id = "comfy",
            displayName = "Comfy",
            keywords = listOf("comfy", "комфі"),
            logoRes = R.drawable.brand_comfy,
            primaryColor = Color(0xFFFF6600),
            secondaryColor = Color(0xFFCC5200),
        ),
        Brand(
            id = "velmart",
            displayName = "Велмарт",
            keywords = listOf("велмарт", "velmart"),
            logoRes = R.drawable.brand_velmart,
            primaryColor = Color(0xFF009639),
            secondaryColor = Color(0xFF00702A),
        ),
        Brand(
            id = "wog",
            displayName = "WOG",
            keywords = listOf("wog", "вог"),
            logoRes = R.drawable.brand_wog,
            primaryColor = Color(0xFFED1C24),
            secondaryColor = Color(0xFFBB151B),
        ),
        Brand(
            id = "okko",
            displayName = "ОККО",
            keywords = listOf("окко", "okko"),
            logoRes = R.drawable.brand_okko,
            primaryColor = Color(0xFF00A651),
            secondaryColor = Color(0xFF007A3D),
        ),
        Brand(
            id = "eva",
            displayName = "EVA",
            keywords = listOf("єва", "ева", "eva"),
            logoRes = R.drawable.brand_eva,
            primaryColor = Color(0xFFFF6600),
            secondaryColor = Color(0xFF7EC845),
        ),
        Brand(
            id = "fishka",
            displayName = "Fishka",
            keywords = listOf("fishka", "фішка", "фишка"),
            logoRes = R.drawable.brand_fishka,
            primaryColor = Color(0xFFE30613),
            secondaryColor = Color(0xFFFFFFFF),
            textColor = Color(0xFFE30613),
        ),
        Brand(
            id = "winetime",
            displayName = "WineTime",
            keywords = listOf("winetime", "wine time", "вайнтайм"),
            logoRes = R.drawable.brand_winetime,
            primaryColor = Color(0xFF1A1A1A),
            secondaryColor = Color(0xFFFF6600),
        ),
    )

    fun findById(id: String): Brand? = brands.find { it.id == id }

    fun findByName(name: String): Brand? {
        val lower = name.lowercase().trim()
        return brands.find { brand ->
            brand.keywords.any { keyword -> lower == keyword || lower.contains(keyword) }
        }
    }

    fun suggest(query: String): List<Brand> {
        if (query.isBlank()) return emptyList()
        val lower = query.lowercase().trim()
        return brands.filter { brand ->
            brand.keywords.any { it.startsWith(lower) } ||
                brand.displayName.lowercase().startsWith(lower)
        }
    }
}
