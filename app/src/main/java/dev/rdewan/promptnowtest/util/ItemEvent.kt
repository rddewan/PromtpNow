package dev.rdewan.promptnowtest.util


/*
created by Richard Dewan 06/11/2021
*/

sealed class ItemEvent {
    object AddItem: ItemEvent()
    object UpdateItem: ItemEvent()
    object DeleteItem: ItemEvent()
    object None: ItemEvent()
}