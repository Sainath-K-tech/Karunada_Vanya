package com.karunada.vanya.data

import com.karunada.vanya.data.model.GuideItem

object GuideData {
    fun getGuides(): List<GuideItem> = listOf(
        GuideItem(
            id = 1,
            animal = "Elephant",
            emoji = "🐘",
            situation = "What to do if an elephant enters your field",
            steps = listOf(
                "Stay calm and do not panic or run suddenly",
                "Move away slowly and quietly — do not shout",
                "Go inside your house and close all doors and windows",
                "Alert your neighbours quietly using phone or SMS",
                "Call the Forest Department helpline: 1800-425-5364",
                "Wait for the elephant to leave on its own — they usually do",
                "Never stand between a mother elephant and her calf"
            ),
            doNot = listOf(
                "Never wave torches or fire near elephants",
                "Never burst crackers to scare them — it panics the herd",
                "Never throw stones or shout — it can provoke a charge",
                "Never try to photograph from close range at night",
                "Never block their path to the forest"
            ),
            backgroundColor = "#E3F2FD"
        ),
        GuideItem(
            id = 2,
            animal = "Leopard / Panther",
            emoji = "🐆",
            situation = "What to do if you see a leopard near your village",
            steps = listOf(
                "Stand tall and maintain eye contact — do not look away",
                "Back away slowly — never turn your back and run",
                "Make yourself appear larger by raising your arms",
                "Speak in a firm, calm voice",
                "If with children, pick them up immediately",
                "Get inside a building as quickly as safely possible",
                "Report the sighting to forest department immediately"
            ),
            doNot = listOf(
                "Never run — it triggers the leopard's chase instinct",
                "Never crouch down or make yourself look small",
                "Never corner a leopard — always leave it an escape route",
                "Never leave small children or pets outside at night",
                "Never approach an injured leopard under any circumstance"
            ),
            backgroundColor = "#FFF8E1"
        ),
        GuideItem(
            id = 3,
            animal = "Tiger",
            emoji = "🐯",
            situation = "What to do if you encounter a tiger in the forest",
            steps = listOf(
                "Freeze immediately — do not move suddenly",
                "Maintain eye contact and stand your ground",
                "Slowly raise your arms to appear larger",
                "Back away very slowly — one step at a time",
                "If in a group, stay together and move as one unit",
                "Make a low, steady sound — do not scream",
                "Report the sighting to forest guards immediately"
            ),
            doNot = listOf(
                "Never run under any circumstances",
                "Never separate from your group",
                "Never enter core forest areas alone or at dawn/dusk",
                "Never carry meat or strong-smelling food in tiger territory",
                "Never approach a tiger even if it appears injured"
            ),
            backgroundColor = "#FBE9E7"
        ),
        GuideItem(
            id = 4,
            animal = "King Cobra",
            emoji = "🐍",
            situation = "What to do if you find a King Cobra near your home",
            steps = listOf(
                "Move away calmly — give it plenty of space",
                "Keep children and pets indoors immediately",
                "Do not try to catch or kill it — it is protected by law",
                "Call a trained snake rescuer or forest department",
                "Mark the location and watch from a safe distance",
                "If bitten, immobilise the affected limb and go to hospital immediately",
                "Note the snake's appearance to help doctors identify venom type"
            ),
            doNot = listOf(
                "Never try to handle or catch a snake yourself",
                "Never apply a tourniquet — it causes more damage",
                "Never try to suck out venom — it does not work",
                "Never kill snakes — they control rat populations that damage crops",
                "Never block a snake's escape route"
            ),
            backgroundColor = "#F1F8E9"
        )
    )
}