import './RadioButton.css'
import StateColour from "./StateColour";

const RadioButton = ({ text, onChange }) => {
    return (
        <label className="radiobutton-label">
            <input
                className="radiobutton-input"
                type="radio"
                name="state"
                value={text.toUpperCase()}
                onChange={onChange}
            />
            <span className="custom-radiobutton" style={{backgroundColor: StateColour(text.toUpperCase())}} />
            {text}
        </label>
    );
};

export default RadioButton;
