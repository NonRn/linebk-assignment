import UserReducer from './UserReducer';
import BannerReducer from './BannerReducer';
import AccountReducer from './AccountReducer';
import DebitCardReducer from './DebitCardReducer';
import TransactionReducer from './TransactionReducer';
import { combineReducers } from 'redux';

export default combineReducers({
    user: UserReducer,
    banner: BannerReducer,
    account: AccountReducer,
    debitCard: DebitCardReducer,
    transaction: TransactionReducer,
});