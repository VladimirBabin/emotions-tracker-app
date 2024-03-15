import './RadioButton.css'

const RadioButton = ({ text, onChange, value, styleColor }) => {
    return (
        <label className="radiobutton-label">
            <input
                className="radiobutton-input"
                type="radio"
                name="state"
                value={value}
                onChange={onChange}

            />
            <span className="custom-radiobutton" style={{backgroundColor: styleColor}} />
            {text}
        </label>
    );
};

export default RadioButton;
