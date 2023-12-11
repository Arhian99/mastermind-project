import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
//bootstrap minimum required css
import 'bootstrap/dist/css/bootstrap.min.css';
// bootstrap sass modifications
import './App.scss';
import {BrowserRouter} from 'react-router-dom'
import { GameProvider } from './Context/GameProvider';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>

    <BrowserRouter>
      <GameProvider>
        <App />
      </GameProvider>
    </BrowserRouter>
    
  </React.StrictMode>
);
