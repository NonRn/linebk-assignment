import HeaderAct from '../components/HeaderAct';
import '../assets/styles/th-bank.css';

const AddMoney = () => {
    return (
        <div className="wrap">
            <HeaderAct />
            <div style={{ textAlign: 'center', padding: '20px', marginTop: '10px' }}>
                <h2>แสกน QR Code <br/> เพื่อโอนเงินเข้าบัญชี** </h2>
                <img 
                    src={require('../assets/add_money_qrcode.jpg')} 
                    alt="QR Code สำหรับเติมเงิน" 
                    style={{ padding: '20px', maxWidth: '90%', maxHeight: '450px' }}
                />

                <p style={{ marginTop: '20px' }}>**บัญชีส่วนตัวผู้ทำ Assignment : โปรดตรวจสอบข้อมูลก่อนโอนเงิน</p>
            </div>
        </div>
    );
};

export default AddMoney; 
