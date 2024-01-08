//package by.clevertec.house.dao.impl;
//
//import by.clevertec.house.dao.HouseDao;
//import by.clevertec.house.entity.HouseEntity;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//public class HouseDaoJdbcTemplateImpl implements HouseDao {
//    private JdbcTemplate jdbcTemplate;
//
//    public HouseDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public HouseEntity getHouseById(Long id) {
//        String sql = "SELECT * FROM houses WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new HouseRowMapper(), id);
//    }
//
//    @Override
//    public List<HouseEntity> getAllHouses() {
//        return null;
//    }
//
//    @Override
//    public void saveHouse(HouseEntity house) {
//
//    }
//
//    @Override
//    public void updateHouse(HouseEntity house) {
//
//    }
//
//    @Override
//    public void deleteHouse(Long id) {
//
//    }
//
//    private static final class HouseRowMapper implements RowMapper<HouseEntity> {
//        public HouseEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//            HouseEntity house = new HouseEntity();
//            house.setId(rs.getLong("id"));
//            return house;
//        }
//    }
//}