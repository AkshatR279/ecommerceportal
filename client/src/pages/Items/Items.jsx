import ItemForm from '../../components/Items/ItemForm/ItemForm';
import ItemList from '../../components/Items/ItemList/ItemList';
import './Items.css';

const Items = () => {
    return (
        <div className="items-container text-light">
            <div className="left-column">
                <ItemForm/>
            </div>
            <div className="right-column">
                <ItemList/>
            </div>
        </div>
    )
}

export default Items;