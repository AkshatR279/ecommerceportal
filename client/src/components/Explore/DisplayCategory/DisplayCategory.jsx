import './DisplayCategory.css';
import Category from '../../SubComponents/Category/Category.jsx';
import {assets} from '../../../assets/assets.js'

const DisplayCategory = ({ categories, selectedCategory, setSelectedCategory }) => {
    return (
        <div className="row g-3" style={{ width: '100%', margin: 0 }}>
            <div key='all' className='col-md-3 col-sm-6' style={{ padding: '0 10px' }}>
                <Category
                    categoryName='All'
                    imgUrl={assets.allCategories}
                    numberOfItems={categories && categories.reduce((acc, cat) => acc + cat.items, 0)} 
                    bgColor='#6c757d'
                    isSelected={selectedCategory === ''}
                    onClick={() => setSelectedCategory('')}
                />
            </div>
            {categories && categories.map(category => (
                <div key={category.categoryId} className='col-md-3 col-sm-6' style={{ padding: '0 10px' }}>
                    <Category
                        categoryName={category.name}
                        imgUrl={category.imgUrl}
                        numberOfItems={category.items} 
                        bgColor={category.bgColor}
                        isSelected={selectedCategory === category.categoryId}
                        onClick={() => setSelectedCategory(category.categoryId)}
                    />
                </div>
            ))}
        </div>
    )
}

export default DisplayCategory;