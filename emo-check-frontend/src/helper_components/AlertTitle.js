import * as React from "react";

export default function AlertTitle (message: string) {
    switch (message) {
        case "stressed-slightly":
            return "We've noticed you logged you're stressed.";
        case "stressed-highly":
            return "We've noticed you've logged you're stressed several times."
        case "stressed-regularly":
            return "We've noticed you're regularly stressed last time."
        case "depressed-slightly":
            return "We've noticed you've logged you're upset."
        case "drained-slightly":
            return "We've noticed you've logged you're drained."
        case "scared-slightly":
            return "We've noticed you've logged you're scared."
        case "resources-low":
            return "We've noticed your resources are getting low."
        case "resources-lowest":
            return "We've noticed your resources are very low."
    }
}