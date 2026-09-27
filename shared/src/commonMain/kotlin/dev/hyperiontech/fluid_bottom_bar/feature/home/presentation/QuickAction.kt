package dev.hyperiontech.fluid_bottom_bar.feature.home.presentation

import fluidbottombar.shared.generated.resources.Res
import fluidbottombar.shared.generated.resources.ic_bookmark
import fluidbottombar.shared.generated.resources.ic_edit
import fluidbottombar.shared.generated.resources.ic_person
import fluidbottombar.shared.generated.resources.ic_search
import fluidbottombar.shared.generated.resources.quick_action_new_post
import fluidbottombar.shared.generated.resources.quick_action_profile
import fluidbottombar.shared.generated.resources.quick_action_saved
import fluidbottombar.shared.generated.resources.quick_action_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class QuickAction(
    val label: StringResource,
) {
    NewPost(label = Res.string.quick_action_new_post),
    Search(label = Res.string.quick_action_search),
    Saved(label = Res.string.quick_action_saved),
    Profile(label = Res.string.quick_action_profile),
}

internal val QuickAction.icon: DrawableResource
    get() =
        when (this) {
            QuickAction.NewPost -> Res.drawable.ic_edit
            QuickAction.Search -> Res.drawable.ic_search
            QuickAction.Saved -> Res.drawable.ic_bookmark
            QuickAction.Profile -> Res.drawable.ic_person
        }
