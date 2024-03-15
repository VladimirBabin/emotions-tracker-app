import React from 'react';
import Popup from "./custom-classes/Popup";
import { useState } from 'react';

function App() {
    const [buttonPopup, setButtonPopup] = useState(false);

    return (
        <div className="App">
            <main>
                <div className="title">
                    <h3>Welcome to emotions tracker</h3>
                    <h6>To log your state tap the button:</h6>
                </div>
                <button className="new-log" onClick={() => setButtonPopup(true)}>+</button>
            </main>
            <div className="popup-div">
                <Popup trigger={buttonPopup} setTrigger={setButtonPopup}/>
            </div>
        </div>
    );
}

export default App;
