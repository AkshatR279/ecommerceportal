import CategoryForm from '../../components/Categories/CategoryForm/CategoryForm';
import CategoryList from '../../components/Categories/CategoryList/CategoryList';
import './Categories.css';

const Categories = () => {
    return (
        <div className="categories-container text-light">
            <div className="left-column">
                <CategoryForm/>
            </div>
            <div className="right-column">
                <CategoryList/>
            </div>
        </div>
    )
}

export default Categories;