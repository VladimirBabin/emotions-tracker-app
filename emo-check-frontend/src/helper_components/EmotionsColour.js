import * as React from "react";

export default function EmotionsColour (emotion: string) {
    switch (emotion) {
        case "HAPPY": return "yellow";
        case "INDIFFERENT": return "skyblue"
        case "SAD": return "mediumpurple"
        case "EXCITED": return "deeppink"
        case "PEACEFUL": return "darkolivegreen"
        case "ANXIOUS": return "cyan"
        case "SATISFIED": return "lightpink"
        case "CONTENT": return "indianred"
        case "DRAINED": return "lightgrey"
        case "PASSIONATE": return "darkorange"
        case "STRESSED": return "powderblue"
        case "ANGRY": return "crimson"
        case "TIRED": return "brown"
        case "HOPEFUL": return "olive"
        case "IRRITATED": return "peru"
        case "SURPRISED": return "sandybrown"
        case "SCARED": return "dimgrey"
        case "JEALOUS": return "darkorange"
    }
}