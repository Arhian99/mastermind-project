import { Route, Routes } from 'react-router-dom';
import './App.css';
import Settings from './Pages/Settings';
import Game from './Pages/Game';
import Home from './Pages/Home';

function App() {
  return (
    <Routes>
      <Route path='/'element={<Home />} /> 
      <Route path='settings' element={<Settings />} />
      <Route path='game' element={<Game />} />

    </Routes>
  );
}

export default App;
