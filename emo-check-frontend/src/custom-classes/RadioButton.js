
const RadioButton = ({ id, text, onChange, value, styleColor }) => {
    return (
        <label htmlFor={id} className="radiobutton-label">
            <input
                className="radiobutton-input"
                type="radio"
                name="state"
                id={id}
                value={value}
                onChange={onChange}

            />
            <span className="custom-radiobutton" style={{backgroundColor: styleColor}} />
            {text}
        </label>
    );
};

export default RadioButton;
