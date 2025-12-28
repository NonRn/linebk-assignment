import { Provider } from 'react-redux';
import { legacy_createStore as createStore} from 'redux'
import rootReducer from './reducers/index'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import StartPage from './pages/StartPage';
import BankMain from './pages/BankMain';
import Login from './pages/Login';
import QRScan from './pages/QRScan'; 
import AddMoney from './pages/AddMoney';
import TotalBalance from './pages/TotalBalance';
import SetMainAccount from './pages/SetMainAccount';
import NotFoundPage from './components/NotFoundPage';

const store = createStore(
  rootReducer
)

function App() {
  return (
    <Provider store={store}>
      <Router>
        <Routes>
          <Route path="/" element={<StartPage />}/>
          <Route path="/bank" element={<BankMain />} />
          <Route path="/login" element={<Login />} />
          <Route path="/qrscan" element={<QRScan />} />
          <Route path="/addmoney" element={<AddMoney />} />
          <Route path="/total-balance" element={<TotalBalance />} />
          <Route path="/set-main-account" element={<SetMainAccount />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Router>
    </Provider>
  );
}

export default App;