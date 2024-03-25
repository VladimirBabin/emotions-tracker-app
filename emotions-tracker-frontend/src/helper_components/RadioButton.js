import './RadioButton.css'
import StateColour from "./StateColour";

const RadioButton = ({ text, onChange, value }) => {
    return (
        <label className="radiobutton-label">
            <input
                className="radiobutton-input"
                type="radio"
                name="state"
                value={value}
                onChange={onChange}
            />
            <span className="custom-radiobutton" style={{backgroundColor: StateColour(value)}} />
            {text}
        </label>
    );
};

export default RadioButton;
