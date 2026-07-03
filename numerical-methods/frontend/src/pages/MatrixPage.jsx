import { useEffect, useState } from "react";
import { getSamples, solveMatrix } from "../api/solver";
import MatrixForm from "../components/MatrixForm";
import MatrixResultPanel from "../components/MatrixResultPanel";

function parseNum(value) {
  return Number(String(value).trim().replace(",", "."));
}

function createInitial(dimension) {
  return Array.from({ length: dimension }, () => "0");
}

function createGeneratedMatrix(dimension) {
  return Array.from({ length: dimension }, (_, rowIndex) => {
    const coefficients = Array.from({ length: dimension }, (_, columnIndex) => {
      if (rowIndex === columnIndex) return "0";
      return String((rowIndex + columnIndex) % 3 + 1);
    });
    const offDiagonalSum = coefficients.reduce((sum, value, columnIndex) => {
      if (columnIndex === rowIndex) return sum;
      return sum + Math.abs(Number(value));
    }, 0);

    coefficients[rowIndex] = String(offDiagonalSum + dimension + 1);
    return [...coefficients, String((rowIndex + 1) * 2)];
  });
}

export default function MatrixPage() {
  const [samples, setSamples] = useState({ methods: { matrix: [] } });
  const [dimension, setDimensionState] = useState(2);
  const [matrix, setMatrix] = useState([
    ["4", "1", "6"],
    ["1", "3", "7"]
  ]);
  const [initialApproximation, setInitialApproximation] = useState(["0", "0"]);
  const [epsilon, setEpsilon] = useState("0.001");
  const [maxIterations, setMaxIterations] = useState("1000");

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let alive = true;

    getSamples("matrix")
      .then((data) => {
        if (alive) setSamples(data);
      })
      .catch((event) => setError(event.message));

    return () => {
      alive = false;
    };
  }, []);

  function setDimension(nextDimension) {
    setDimensionState(nextDimension);
    setMatrix((current) =>
      Array.from({ length: nextDimension }, (_, rowIndex) =>
        Array.from({ length: nextDimension + 1 }, (_, columnIndex) => {
          if (current[rowIndex]?.[columnIndex] !== undefined) return current[rowIndex][columnIndex];
          return rowIndex === columnIndex ? "1" : "0";
        })
      )
    );
    setInitialApproximation((current) =>
      Array.from({ length: nextDimension }, (_, index) => current[index] ?? "0")
    );
    setResult(null);
  }

  function useExample() {
    setDimensionState(2);
    setMatrix([
      ["4", "1", "6"],
      ["1", "3", "7"]
    ]);
    setInitialApproximation(["0", "0"]);
    setEpsilon("0.001");
    setMaxIterations("1000");
    setResult(null);
  }

  function generateMatrix() {
    setMatrix(createGeneratedMatrix(dimension));
    setInitialApproximation(createInitial(dimension));
    setResult(null);
  }

  async function handleSolve(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = {
        matrix: matrix.map((row) => row.map(parseNum)),
        epsilon: parseNum(epsilon),
        initialApproximation: initialApproximation.map(parseNum),
        maxIterations: Number(maxIterations)
      };

      const response = await solveMatrix(payload);
      setResult(response);
    } catch (event) {
      setError(event.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Системы линейных алгебраических уравнений</h2>
      <MatrixForm
        samples={samples}
        dimension={dimension}
        setDimension={setDimension}
        matrix={matrix}
        setMatrix={setMatrix}
        initialApproximation={initialApproximation}
        setInitialApproximation={setInitialApproximation}
        epsilon={epsilon}
        setEpsilon={setEpsilon}
        maxIterations={maxIterations}
        setMaxIterations={setMaxIterations}
        onSubmit={handleSolve}
        loading={loading}
        onUseExample={useExample}
        onGenerate={generateMatrix}
      />
      <MatrixResultPanel result={result} error={error} />
    </div>
  );
}
