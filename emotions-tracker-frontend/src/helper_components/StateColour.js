export default function StateColour (emotion: string) {
    switch (emotion) {
        case "AWFUL": return "mediumslateblue";
        case "BAD": return "cornflowerblue"
        case "OK": return "yellowgreen"
        case "GOOD": return "mediumseagreen"
        case "EXCELLENT": return "seagreen"
        default: return "white"
    }
}