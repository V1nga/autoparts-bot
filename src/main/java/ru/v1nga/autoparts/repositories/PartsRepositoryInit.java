package ru.v1nga.autoparts.repositories;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.v1nga.autoparts.entities.PartEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class PartsRepositoryInit {

    private final DataSource dataSource;
    private final PartsRepository partsRepository;

    @PostConstruct
    public void init() throws SQLException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                ALTER TABLE car_parts
                ADD COLUMN IF NOT EXISTS number_vector tsvector
            """);

            stmt.execute("""
                UPDATE car_parts
                SET number_vector = to_tsvector('simple', number)
            """);

            stmt.execute("""
                CREATE INDEX IF NOT EXISTS idx_number_vector
                ON car_parts USING GIN (number_vector)
            """);

            stmt.execute("""
                DROP TRIGGER IF EXISTS trg_number_vector_update
                ON car_parts
            """);

            stmt.execute("""
                CREATE TRIGGER trg_number_vector_update
                BEFORE INSERT OR UPDATE ON car_parts
                FOR EACH ROW
                EXECUTE FUNCTION tsvector_update_trigger(
                    'number_vector', 'pg_catalog.simple', 'number'
                )
            """);
        }

        initDevData();
    }

    private void initDevData() {
        List<PartEntity> parts = List.of(
           new PartEntity(0, "17220-RNA-A00", "Фильтр воздушный (Honda)"),
           new PartEntity(0, "31100-RNA-A01", "Генератор (Honda)"),
           new PartEntity(0, "06192-PNA-305", "Комплект масляного фильтра (Honda)"),
           new PartEntity(0, "15400-PLM-A02", "Масляный фильтр (Honda)"),
           new PartEntity(0, "80310-SDA-A01", "Компрессор кондиционера (Honda)"),
           new PartEntity(0, "19301-PNA-003", "Термостат (Honda)"),
           new PartEntity(0, "19500-RAA-A00", "Шланг радиатора (Honda)"),
           new PartEntity(0, "30520-RRA-007", "Катушка зажигания (Honda)"),
           new PartEntity(0, "31170-RCA-A02", "Ремень генератора (Honda)"),
           new PartEntity(0, "12341-RCA-A00", "Прокладка клапанной крышки (Honda)"),

           new PartEntity(0, "90915-YZZD3", "Масляный фильтр (Toyota)"),
           new PartEntity(0, "17801-0H050", "Воздушный фильтр (Toyota)"),
           new PartEntity(0, "04465-0E010", "Тормозные колодки (Toyota)"),
           new PartEntity(0, "17801-37021", "Воздушный фильтр двигателя (Toyota)"),
           new PartEntity(0, "90919-01253", "Свеча зажигания (Toyota)"),
           new PartEntity(0, "90080-19016", "Болт сливной пробки (Toyota)"),
           new PartEntity(0, "90916-03100", "Термостат (Toyota)"),
           new PartEntity(0, "27060-0T020", "Генератор (Toyota)"),
           new PartEntity(0, "27060-0H020", "Стартер (Toyota)"),
           new PartEntity(0, "43512-0D020", "Тормозной диск передний (Toyota)"),

           new PartEntity(0, "03L131512CF", "Клапан EGR (Volkswagen)"),
           new PartEntity(0, "06A905115D", "Катушка зажигания (Volkswagen)"),
           new PartEntity(0, "1J0615301M", "Главный тормозной цилиндр (Volkswagen)"),
           new PartEntity(0, "06F129101L", "Корпус дроссельной заслонки (Volkswagen)"),
           new PartEntity(0, "06A121132E", "Помпа водяная (Volkswagen)"),
           new PartEntity(0, "03G906051D", "Датчик массового расхода воздуха (Volkswagen)"),
           new PartEntity(0, "038131501AF", "Клапан EGR (Volkswagen)"),
           new PartEntity(0, "1J0145762AA", "Патрубок интеркулера (Volkswagen)"),
           new PartEntity(0, "1K0615124", "Тормозной суппорт задний (Volkswagen)"),
           new PartEntity(0, "06B109509J", "Цепь ГРМ (Volkswagen)"),

            // Ещё 70 элементов:
           new PartEntity(0, "12341-RCA-A02", "Прокладка клапанной крышки (Honda)"),
           new PartEntity(0, "15610-PLM-A01", "Клапан PCV (Honda)"),
           new PartEntity(0, "37820-PND-A01", "Блок ECU (Honda)"),
           new PartEntity(0, "91214-RTA-003", "Сальник коленвала (Honda)"),
           new PartEntity(0, "31175-PNA-003", "Натяжитель ремня (Honda)"),
           new PartEntity(0, "19305-REA-305", "Крышка радиатора (Honda)"),
           new PartEntity(0, "91503-SZ3-003", "Клипса обшивки (Honda)"),
           new PartEntity(0, "38513-SAA-003", "Реле вентилятора (Honda)"),
           new PartEntity(0, "38300-SDA-A03", "Реле стартера (Honda)"),
           new PartEntity(0, "38810-RTA-003", "Компрессор кондиционера (Honda)"),

           new PartEntity(0, "17801-0T020", "Воздушный фильтр (Toyota)"),
           new PartEntity(0, "90915-YZZF1", "Масляный фильтр (Toyota)"),
           new PartEntity(0, "04152-YZZA1", "Картридж масляного фильтра (Toyota)"),
           new PartEntity(0, "31400-0F010", "Стартер (Toyota)"),
           new PartEntity(0, "89661-0R010", "Блок управления двигателем (Toyota)"),
           new PartEntity(0, "90982-05035", "Реле (Toyota)"),
           new PartEntity(0, "90080-91103", "Прокладка сливной пробки (Toyota)"),
           new PartEntity(0, "90942-02049", "Пробка радиатора (Toyota)"),
           new PartEntity(0, "84306-0K051", "Шлейф airbag (Toyota)"),
           new PartEntity(0, "23300-0L010", "Топливный насос (Toyota)"),

           new PartEntity(0, "03C121132C", "Водяной насос (Volkswagen)"),
           new PartEntity(0, "038131501AN", "Клапан EGR (Volkswagen)"),
           new PartEntity(0, "03L109243B", "Натяжитель цепи (Volkswagen)"),
           new PartEntity(0, "03L253056G", "Турбокомпрессор (Volkswagen)"),
           new PartEntity(0, "1K0129620D", "Интеркулер (Volkswagen)"),
           new PartEntity(0, "06F133062Q", "Клапан вентиляции картера (Volkswagen)"),
           new PartEntity(0, "1K0614517BD", "Тормозной диск задний (Volkswagen)"),
           new PartEntity(0, "5Q0121251AK", "Радиатор (Volkswagen)"),
           new PartEntity(0, "1J0959455F", "Реле вентилятора (Volkswagen)"),
           new PartEntity(0, "1K0407182", "Шаровая опора (Volkswagen)"),

           new PartEntity(0, "12513-RCA-A00", "Прокладка ГБЦ (Honda)"),
           new PartEntity(0, "91213-R70-A01", "Сальник распредвала (Honda)"),
           new PartEntity(0, "91302-GE0-000", "Кольцо уплотнительное (Honda)"),
           new PartEntity(0, "22810-RAA-A01", "Выжимной подшипник (Honda)"),
           new PartEntity(0, "22870-RNA-A00", "Рабочий цилиндр сцепления (Honda)"),
           new PartEntity(0, "43022-SNA-A02", "Тормозные колодки задние (Honda)"),
           new PartEntity(0, "45251-SNA-A01", "Тормозной диск передний (Honda)"),
           new PartEntity(0, "12520-RCA-A01", "Маслоприемник (Honda)"),
           new PartEntity(0, "14510-RCA-A01", "Натяжитель цепи (Honda)"),
           new PartEntity(0, "15220-RAA-A01", "Клапан VTEC (Honda)"),

           new PartEntity(0, "27060-0T040", "Генератор (Toyota)"),
           new PartEntity(0, "44310-0D060", "ШРУС (Toyota)"),
           new PartEntity(0, "43420-0D060", "Привод в сборе (Toyota)"),
           new PartEntity(0, "48131-0D060", "Амортизатор передний (Toyota)"),
           new PartEntity(0, "48530-0D060", "Амортизатор задний (Toyota)"),
           new PartEntity(0, "47730-0D060", "Суппорт тормозной передний (Toyota)"),
           new PartEntity(0, "48190-0D060", "Пружина передняя (Toyota)"),
           new PartEntity(0, "48820-0D060", "Стабилизатор передний (Toyota)"),
           new PartEntity(0, "69210-0D060", "Замок двери (Toyota)"),
           new PartEntity(0, "85967-0D060", "Блок розжига фар (Toyota)"),

           new PartEntity(0, "1K0615301AH", "Тормозной цилиндр главный (Volkswagen)"),
           new PartEntity(0, "06J115403Q", "Масляный насос (Volkswagen)"),
           new PartEntity(0, "06F129101L", "Дроссельная заслонка (Volkswagen)"),
           new PartEntity(0, "1K0129654", "Шланг радиатора (Volkswagen)"),
           new PartEntity(0, "06A121133D", "Термостат (Volkswagen)"),
           new PartEntity(0, "3B1837016AS", "Замок двери (Volkswagen)"),
           new PartEntity(0, "1K0959455F", "Реле вентилятора (Volkswagen)"),
           new PartEntity(0, "5Q0907697C", "Датчик давления шин (Volkswagen)"),
           new PartEntity(0, "1K0953519A", "Переключатель подрулевой (Volkswagen)"),
           new PartEntity(0, "1J0959653C", "Блок управления AIRBAG (Volkswagen)")
        );

        partsRepository.saveAll(parts);
    }
}
